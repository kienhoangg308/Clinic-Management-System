
package com.clinic.admin.record;

import com.clinic.admin.appointment.AppointmentNotFoundException;
import com.clinic.admin.customer.CustomerRepository;
import com.clinic.admin.customerservice.CustomerServiceRepository;
import com.clinic.admin.service.CLinicServiceRepository;
import com.clinic.common.entity.*;
import com.clinic.common.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class RecordService {

    public static final int RECORDS_PER_PAGE = 4;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private CustomerServiceRepository customerServiceRepository;

    @Autowired
    private CustomerRepository customerRepository;




    public void save(Record record) {
        List<ClinicService> clinicServices = record.getServices();
        Integer cusId = record.getAppointment().getCustomer().getId();
        Customer customer = customerRepository.findById(cusId).get();
        List<CustomerService> customerServices = customerServiceRepository.findOldestWithRemainingAmountForServices(customer, clinicServices);
        for(CustomerService customerService : customerServices){
            customerService.setRemainingAmount(customerService.getRemainingAmount() - 1);
        }
        customerServiceRepository.saveAll(customerServices);
        recordRepository.save(record);
    }

    public Page<Record> listByPage(int pageNum, String sortDir, String sortField, String keyword) {

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

        if (keyword != null) {
            return recordRepository.findAll(keyword, pageable);
        }

        return recordRepository.findAll(pageable);
    }

    public Record get(Integer id) throws RecordNotFoundException {
        try {
            return recordRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new RecordNotFoundException("Could not find any record with ID " + id);
        }
    }


}


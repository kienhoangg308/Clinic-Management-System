package com.clinic.admin.customerservice;

import com.clinic.admin.customer.CustomerRepository;
import com.clinic.admin.service.CLinicServiceRepository;
import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceService {

    @Autowired
    private CustomerServiceRepository customerServiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CLinicServiceRepository cLinicServiceRepository;

    public CustomerService saveService(CustomerService customerService){
        return customerServiceRepository.save(customerService);
    }


    public boolean checkServiceAvailability(Integer customerId, List<Integer> serviceIds) {
        Customer customer = customerRepository.findById(customerId).get();
        List<ClinicService> clinicServices = new ArrayList<>();


        for (Integer serviceId : serviceIds) {
            // Retrieve the clinic service object
            ClinicService service = cLinicServiceRepository.findById(serviceId).get();
            clinicServices.add(service);
        }

            // Check if there is an entry with remaining sessions for this service and customer
            List<CustomerService> availableServices = customerServiceRepository.findOldestWithRemainingAmountForServices(customer, clinicServices);
            if (availableServices.isEmpty() ) {
                // If there's no available service or the remaining sessions are not greater than 0, return false
                return false;
            }
            for(CustomerService customerService : availableServices){
                if(customerService.getRemainingAmount() <= 0){
                    return false;
                }
            }

        // If all services have remaining sessions, return true
        return true;
    }
}

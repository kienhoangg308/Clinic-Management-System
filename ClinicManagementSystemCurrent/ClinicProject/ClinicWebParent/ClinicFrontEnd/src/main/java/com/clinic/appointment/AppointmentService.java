package com.clinic.appointment;

import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.AppointmentStatus;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.Specialization;
import com.clinic.customer.CustomerRepository;
import com.clinic.specialization.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Specialization> listSpecializations() {
        return (List<Specialization>) specializationRepository.findAll();
    }

    public Appointment save(Appointment appointment){
        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }



    public Specialization findSpecializationByName(String specializationName) {
        Specialization specialization = specializationRepository.findByName(specializationName);
        return specialization;
    }


    public Customer findByCustomerEmail(String email) {
        Customer customer = customerRepository.findByCustomerEmail(email);
        return customer;
    }
}

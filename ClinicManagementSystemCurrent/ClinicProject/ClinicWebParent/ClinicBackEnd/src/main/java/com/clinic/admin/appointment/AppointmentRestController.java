package com.clinic.admin.appointment;


import com.clinic.admin.customer.CustomerNotFoundException;
import com.clinic.admin.customerservice.CustomerServiceService;
import com.clinic.admin.service.ServiceDTO;
import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AppointmentRestController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CustomerServiceService customerServiceService;


    @GetMapping("/appointments/{appointmentId}/specialization/{specializationId}/select/services")
    public List<ServiceDTO> loadServiceBySpecialization(@PathVariable(name = "specializationId") Integer specializationId,
                                                  @PathVariable(name = "appointmentId") Integer appointmentId) {


        List<ServiceDTO> result = appointmentService.loadService(appointmentId,specializationId);
        return result;
    }

    @PostMapping("/appointments/assignment")
    public AssignmentOutputModel assignServicesToAppointment(@RequestBody AssignmentInputModel input) {
        System.out.println("get first place");
        AssignmentOutputModel output = appointmentService.assignServicesToAppointment(input);
        return output;
    }

    @PostMapping("/appointments/customerservice/save")
    public ResponseEntity<?> saveCustomerService(CustomerService customerService, RedirectAttributes ra) throws CustomerNotFoundException, AppointmentNotFoundException {

        String email = customerService.getCustomer().getEmail();
        Appointment appointment = appointmentService.get(customerService.getAppointment().getId());
        Customer customer = appointmentService.findByCustomerEmail(email);
        ClinicService clinicService = customerService.getService();

        customerService.setRemainingAmount(clinicService.getTotalAmount());
        customerService.setTotalAmount(clinicService.getTotalAmount());
        customerService.setPrice(clinicService.getPrice());
        customerService.setAppointment(customerService.getAppointment());

        AssignmentOutputModel assignmentOutputModel = new AssignmentOutputModel();
        assignmentOutputModel.setServiceIds(customerService.getService().getId().toString());
        customerService.setCustomer(customer);
        customerServiceService.saveService(customerService);
        //ra.addFlashAttribute("message", "The category has been saved successfully.");
//        return "redirect:/appointments";

//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "success");
//        response.put("message", "Customer service saved successfully");
//        response.put("customerId", customerService.getId()); // Assuming you want to return the ID
//
//        // Return the response object as JSON
//        return ResponseEntity.ok(response);
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/ClinicAdmin/appointments/" + customerService.getAppointment().getId() + "/customerservice/create");
        return ResponseEntity.ok(response);
    }
}

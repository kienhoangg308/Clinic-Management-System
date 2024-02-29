package com.clinic.admin.customerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerServiceRestController {

    @Autowired
    private CustomerServiceService customerServiceService;

    @PostMapping("/customerservices/check_availability")
    public String checkServiceAvailability(@RequestParam("customerId") Integer customerId, @RequestParam("services") List<Integer> serviceIds) {
        // Assuming you have a method in your service to check the availability of multiple services at once
        boolean allServicesAvailable = customerServiceService.checkServiceAvailability(customerId, serviceIds);
        return allServicesAvailable ? "OK" : "Unavailable";
    }
}
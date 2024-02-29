package com.clinic.admin.record;

import com.clinic.admin.customerservice.CustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecordRestController {

    @Autowired
    private CustomerServiceService customerServiceService;

//    @PostMapping("/records/check_availability")
//    public String checkServiceAvailability(@Param("customerId") Integer customerId, @Param("services") List<Integer> serviceIds) {
//        // Assuming you have a method in your service to check the availability of multiple services at once
//        System.out.println(customerId);
//        boolean allServicesAvailable = customerServiceService.checkServiceAvailability(customerId, serviceIds);
//        return allServicesAvailable ? "OK" : "Unavailable";
//    }

    @GetMapping("/records/check_availability")
    public String checkServiceAvailability(
            @RequestParam("customerId") Integer customerId,
            @RequestParam("services") List<Integer> serviceIds
    ) {
        // Assuming you have a method in your service to check the availability of multiple services at once
        System.out.println(customerId);
        boolean allServicesAvailable = customerServiceService.checkServiceAvailability(customerId, serviceIds);
        return allServicesAvailable ? "OK" : "Unavailable";
    }
}
package com.clinic.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClinicServiceRestController {

    @Autowired
    private ClinicServiceService service;

    @PostMapping("/services/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return service.checkUnique(id, name);
    }
}

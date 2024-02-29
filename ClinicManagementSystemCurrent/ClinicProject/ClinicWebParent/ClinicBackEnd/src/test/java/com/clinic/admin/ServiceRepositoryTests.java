package com.clinic.admin;


import com.clinic.admin.service.CLinicServiceRepository;
import com.clinic.common.entity.ClinicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ServiceRepositoryTests {

    @Autowired
    private CLinicServiceRepository serviceRepository;

    @Test
    public void testCreateService(){

        LocalDateTime date = LocalDateTime.now();
        ClinicService service = new ClinicService("cham soc da", 230);
        ClinicService savedService = serviceRepository.save(service);

        assertThat(savedService.getId()).isGreaterThan(0);
    }
}

package com.clinic.admin;

import com.clinic.admin.specialization.SpecializationRepository;
import com.clinic.admin.user.RoleRepository;
import com.clinic.common.entity.Role;
import com.clinic.common.entity.Specialization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SpecializationTests {

    @Autowired
    private SpecializationRepository specializationRepository;

    @Test
    public void testCreateRole(){
        Specialization specialization = new Specialization("Dermatological Treatment", "provide skin beauty service");
        Specialization savedRole = specializationRepository.save(specialization);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }
}


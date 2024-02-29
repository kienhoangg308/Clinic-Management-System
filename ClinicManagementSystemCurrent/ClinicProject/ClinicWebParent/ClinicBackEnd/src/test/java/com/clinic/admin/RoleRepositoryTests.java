package com.clinic.admin;

import com.clinic.admin.user.RoleRepository;
import com.clinic.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole(){
        Role roleAdmin = new Role("Staff", "do some stuff");
        Role savedRole = roleRepository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }
}

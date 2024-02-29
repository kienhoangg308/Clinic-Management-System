package com.clinic;

import com.clinic.common.entity.Customer;
import com.clinic.common.entity.Role;
import com.clinic.common.entity.User;
import com.clinic.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123123123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Customer customer = new Customer(encodedPassword,"kien", "01299139952","kiensuper@gmail.com","ha noi","kien");
        customer.setEnabled(true);

        Customer savedUser = repo.save(customer);

        assertThat(savedUser.getId()).isGreaterThan(0);

    }
}

package com.clinic.admin;

import com.clinic.admin.appointment.AppointmentRepository;
import com.clinic.admin.customer.CustomerRepository;
import com.clinic.admin.user.RoleRepository;
import com.clinic.admin.user.UserRepository;
import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.Role;
import com.clinic.common.entity.Specialization;
import com.clinic.common.entity.User;
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
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testCreateUser(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123123123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Role roleDoctor = entityManager.find(Role.class, 1);
        Specialization specialization = entityManager.find(Specialization.class, 1);
        User doctor = new User("huyhoang","huyhoang@gmail.com",encodedPassword,"kien", "hoang", roleDoctor);
        doctor.setEnabled(true);
        doctor.setSpecialization(specialization);
        doctor.generateCode(roleDoctor);

        System.out.println(roleDoctor);
        System.out.println(doctor);
        User savedUser = repo.save(doctor);
        if(roleDoctor.getName().equals("Doctor")){
            System.out.println("true");
        }else{
            System.out.println("false");
        }

        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void haveUser(){
        User user = repo.getUserByEmail("kien@gmail.com");
        System.out.println(user);

    }

    @Test
    public void update(){
        int id =5;
        repo.updateUserStatus(id, true);
    }

    @Test
    public void updateStaffRole(){
        int id =10;
        User user = repo.findById(id).get();
        Role role = roleRepository.findById(3).get();
        user.setRole(role);
        repo.save(user);
    }

    @Test
    public void setCustomer(){
        int id =6;
        Appointment appointment = appointmentRepository.findById(24).get();
        appointment.setCustomer(customerRepository.findById(6).get());
        appointmentRepository.save(appointment);
    }

    @Test
    public void delete(){
        repo.deleteById(2);


    }

    @Test
    public void deleteAppointment(){
        repo.deleteById(6);


    }
}

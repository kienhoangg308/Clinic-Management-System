package com.clinic.user;

import com.clinic.common.entity.Role;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> listAllDoctor() {
        return userRepository.findByRoleName("Doctor");
    }
}

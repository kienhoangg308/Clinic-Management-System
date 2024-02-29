package com.clinic.admin.security;

import com.clinic.admin.user.UserRepository;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ClinicUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        System.out.println(user);

        if (user != null) {
            return new ClinicUserDetails(user);
        }

        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }
}

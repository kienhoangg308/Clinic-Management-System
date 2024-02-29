package com.clinic.admin.security;

import com.clinic.common.entity.Role;
import com.clinic.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClinicUserDetails implements UserDetails {

    private static final long serialVersionUID = -6286335737603281629L;
    private User user;


    public ClinicUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();

        List<SimpleGrantedAuthority> authories = new ArrayList<>();

        authories.add(new SimpleGrantedAuthority(role.getName()));


        return authories;
    }

    public boolean hasRole(String roleName) {
        return user.hasRole(roleName);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


    public Integer getUserId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

    public String getFullname() {
        return this.user.getLastName() + " " + this.user.getFirstName();
    }
}

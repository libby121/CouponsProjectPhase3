package com.example.demo.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AdminUserDetails implements UserDetails {
    @Value("${sys.username}")
    private String adminUsername;
    @Value("${sys.password}")
    private String adminPassword;

    public AdminUserDetails() {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminUsername = "admin";
        this.adminPassword = "1234";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
        (new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    public String getPassword() {
        return "1234";
    }

    @Override
    public String getUsername() {
        return "admin";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

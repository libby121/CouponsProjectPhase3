package com.example.demo.service;

import com.example.demo.model.AdminUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {
    @Value("${sys.username}")
    private String adminUsername;
    @Value("${sys.password}")
    private String adminPassword;
     public UserDetails loadUserByUsername(String username, String password) throws UsernameNotFoundException {
         if(!adminUsername.equals(username) || !adminPassword.equals(password)){

            throw new BadCredentialsException("not an admin");}


        return new AdminUserDetails();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!adminUsername.equals(username)){

            throw new BadCredentialsException("not an admin");}


        return new AdminUserDetails();
    }
}

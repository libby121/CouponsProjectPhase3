package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.model.MyUserDetails;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * implementation class of UserDetailsService
 * defines how to load a user of type Customer
 * returns customized UserDetails object
 */
@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email).orElseThrow();
        return new MyUserDetails(customer);
    }
}

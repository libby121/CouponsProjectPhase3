package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.exceptions.loginException;
import com.example.demo.model.MyUserDetails;
import com.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * implementation class of UserDetailsService
 * defines how to load a user of type Company
 * returns customized UserDetails object
 */
@Service
public class CompanyDetailsService implements UserDetailsService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Company company= companyRepository.findByuserName(username).orElseThrow();
//        if(company == null) {
//            System.out.println("no user for ya");
//            throw new UsernameNotFoundException("no 2");
//        }
        return new MyUserDetails(company
        );
    }
}

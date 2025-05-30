package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Coupon;
import com.example.demo.entity.Customer;
import com.example.demo.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface AdminService {

//     String login( String username,String password);
     void addCompany(Company comp) throws companyExistsException;
     void updateCompany(Company comp) throws CompanyDoesNotExistException, unmodifiedCompanyNameException;
     void deleteCompany(UUID id);
     List<Company> getAllCompanies();
     void updateCustomer(Customer customer) throws CustomerDoesnotExistException;
     void deleteCustomer(UUID id) throws CustomerDoesnotExistException;
     List<Customer> getAllCustomers() ;


     Customer getOneCustomer(UUID id)
            throws CustomerDoesnotExistException ;

     List<Coupon> getAllCoupons() ;

     Coupon getOneCoupon(int coupId)
            throws CouponDoesnotExistException ;

     Company getOneCompany(String token,  UUID companyId)
       ;

    void addCustomer(String token,Customer customer) ;

    Company getCompanyByID(UUID companyId);
}

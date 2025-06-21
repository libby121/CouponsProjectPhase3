package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Coupon;
import com.example.demo.entity.Customer;
import com.example.demo.exceptions.*;
 import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * /**
 * service tag is a specific @component(=>signals classes to be managed by spring
 *          context with the functionality of dependency injection.)
 * service tag marks for spring a facade class, used for defining the business
 *          logic. Since each connected user will get his own facade eventually,
 *          and both company and customer facades hold a variable of
 *          identification,of which the returned facade is dependent upon, the
 *          service scope is set to prototype. So that when a user gets a facade
 *          after login, he gets an object that contains his individual possible
 *          functionalities.
 */

@Service
@Scope(value = "prototype")
public class AdminServiceImpl  implements AdminService {

    private final CompanyRepository companyRepo;
    private final CouponRepository couponRepo;
    private final CustomerRepository customerRepo;

    public AdminServiceImpl(CompanyRepository companyRepository, CouponRepository couponRepository, CustomerRepository customerRepository) {
        this.companyRepo = companyRepository;
        this.couponRepo = couponRepository;
        this.customerRepo = customerRepository;
    }


    public void addCompany(Company comp) throws companyExistsException {

        for (Company c : companyRepo.findAll()) {
            if (c.getEmail().equals(comp.getEmail()) || c.getUserName().equals(comp.getUserName()))
                throw new companyExistsException();
        }
        companyRepo.save(comp);

    }

    public void updateCompany(Company comp) throws CompanyDoesNotExistException, unmodifiedCompanyNameException {

        Company compa = getCompanyByID(comp.getId());
        if (!compa.getUserName().equals(comp.getUserName()))
            throw new unmodifiedCompanyNameException();

        else
            companyRepo.updateCompany(comp);

    }

    /**
     * Deletion of a company requires first the deletion of the company coupons
     * purchases(represented in the many-to-many table of customers_vs_coupons),
     * the deletion of the company coupons from customers carts,
     * and the deletion of company coupons from
     * the coupons table.
     *
     * @param id- company id
     */
    public void deleteCompany(UUID id) {

        for (Coupon c : couponRepo.findByCompanyId(id)) {


            if (c.getCustomers() != null) {
                for (Customer cu : c.getCustomers()) {



                    couponRepo.deleteCouponPurchase(cu.getId(), c.getId());
                }
            }

            couponRepo.deleteById(c.getId());

        }

        companyRepo.deleteById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Company getCompanyByID(UUID id) {
        return companyRepo.findById(id).orElse(null);
    }

    public void addCustomer(Customer customer) throws CustomerExistsException {
        for (Customer c : customerRepo.findAll()) {
            if (c.getEmail().equals(customer.getEmail()))
                throw new CustomerExistsException();
        }
        customer.setRevenue(0);
        customer.setPrime(false);
        customerRepo.save(customer);
    }

    public void updateCustomer(Customer customer) throws CustomerDoesnotExistException {
        if (customerRepo.existsById(customer.getId()))

            customerRepo.save(customer);
        else
            throw new CustomerDoesnotExistException();

    }

    /**
     * A deletion of a customer requires the preceding deletion of the customer's
     * purchases and cart.
     *
     * @param id- customer id
     */
    public void deleteCustomer(UUID id) throws CustomerDoesnotExistException {
        Customer c = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
        if (c.getCoupons() != null) {
            for (Coupon coup : c.getCoupons()) {
                couponRepo.deleteCouponPurchase(c.getId(), coup.getId());
            }
        }

        customerRepo.deleteById(id);

    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer getOneCustomer(UUID id) throws CustomerDoesnotExistException {
        return customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    public Coupon getOneCoupon(int coupId) throws CouponDoesnotExistException {
        return couponRepo.findById(coupId).orElseThrow(CouponDoesnotExistException::new);
    }

    @Override
    public Company getOneCompany(String token, UUID companyId) {
        return null;
    }

    @Override
    public void addCustomer(String token, Customer customer) {

    }

}

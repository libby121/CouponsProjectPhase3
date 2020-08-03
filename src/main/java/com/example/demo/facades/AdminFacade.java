package com.example.demo.facades;

import java.util.List;

import javax.xml.ws.ServiceMode;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.Customer;
import com.example.demo.exceptions.CustomerExistsException;
import com.example.demo.exceptions.CompanyDoesNotExistException;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CustomerDoesnotExistException;
import com.example.demo.exceptions.companyExistsException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.unmodifiedCompanyNameException;

/**
 * /**
 * 
 * @Service is a specific @component(=>signals classes to be managed by spring
 *          context with the functionality of dependency injection.)
 * @service marks for spring a facade class, used for defining the business
 *          logic. Since each connected user will get his own facade eventually,
 *          and both company and customer facades hold a variable of
 *          identification,of which the returned facade is dependent upon, the
 *          service scope is set to prototype. So that when a user gets a facade
 *          after login, he gets an object that contains his individual possible
 *          functionalities.
 */

@Service
@Scope(value = "prototype")
public class AdminFacade extends Facade {

	@Override
	public boolean login(String email, String password) {
		return (email.equals("com.admin@admin") && password.equals("admin"));

	}

	public void addCompany(Company comp) throws companyExistsException {

		for (Company c : companyRepo.findAll()) {
			if (c.getEmail().equals(comp.getEmail()) || c.getName().equals(comp.getName()))
				throw new companyExistsException();
		}
		companyRepo.save(comp);

	}

	public void updateCompany(Company comp) throws CompanyDoesNotExistException, unmodifiedCompanyNameException {

		Company compa = getCompanyByID(comp.getId());
		if (!compa.getName().equals(comp.getName()))
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
	 * @param id
	 * @throws companyDoesnotExistException
	 */

	public void deleteCompany(int id) {

		for (Coupon c : couponRepo.findByCompanyId(id)) {

			 
			couponRepo.deleteCouponFromCart(c.getId());
			if (c.getCustomers() != null) {
				for (Customer cu : c.getCustomers()) {



					couponRepo.deleteCouponPurchase(c.getId(), cu.getId());
				}
			}

			couponRepo.deleteById(c.getId());

		}

		companyRepo.deleteById(id);
	}

	public List<Company> getAllCompanies() {
		return companyRepo.findAll();
	}

	public Company getCompanyByID(int id) throws CompanyDoesNotExistException {
		return companyRepo.findById(id).orElseThrow(CompanyDoesNotExistException::new);
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
	 * @param id
	 */
	public void deleteCustomer(int id) throws CustomerDoesnotExistException {
		Customer c = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
		if (c.getCoupons() != null) {
			for (Coupon coup : c.getCoupons()) {
				couponRepo.deleteCouponPurchase(c.getId(), coup.getId());
			}
		}
		if (c.getCart() != null) {
			for (Coupon coup : c.getCart().getCoupons()) {
				couponRepo.deleteCouponFromCart(coup.getId());
			}
			cartRepo.deleteCustomerCart(id);
		}

		customerRepo.deleteById(id);

	}

	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}

	public Customer getOneCustomer(int id) throws CustomerDoesnotExistException {
		return customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
	}

	public List<Coupon> getAllCoupons() {
  		return couponRepo.findAll();
 	}

	public Coupon getOneCoupon(int coupId) throws CouponDoesnotExistException {
		return couponRepo.findById(coupId).orElseThrow(CouponDoesnotExistException::new);
	}

}

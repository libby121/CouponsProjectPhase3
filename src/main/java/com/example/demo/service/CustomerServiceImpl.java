package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exceptions.*;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * 
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
@Scope("prototype")//?
  public class CustomerMyService implements MyService {

	private UUID id;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtService jwtService;
	@Autowired
	private PasswordEncoder passwordEncoder;//?
	//@PreAuthorize("ADMIN")


	public String verify(String username, String password){

		Authentication authentication =
				authManager.authenticate(new
						UsernamePasswordAuthenticationToken(username,
						password));

	//check if has any role


		if(authentication.isAuthenticated())
			return jwtService.generateToken(username);
		return "User was not authenticated. Wrong " +
				"username or password";

	}

	public CustomerDTO register(String username, String password, String email){
		Customer c = new Customer(username,password,email);
		c.setPassword(passwordEncoder.encode(c.getPassword()));
		customerRepo.save(c);
		return convertToDTO(c);


	}



	/**
	 * Before activating the purchase, it is checked whether the buyer is a prime
	 * customer. if he is then the coupon's price is 5% lower. A customer becomes
	 * prime if his revenue to the companies gets to 2000 ILS, after every
	 * purchase it is checked whether the customer should become a prime customer.Company's
	 * balance and coupon's amount are also modified.
	 * 
	 * @param coupId- coupon id
	 * @throws CustomerDoesnotExistException- no customer
	 * @throws CouponOutOfStockException- no more coupons in stock
	 * @throws PurchaseDuplicationException - customer already purchased the product
	 * @throws couponExpiredException - coupon expired
	 * @throws CouponDoesnotExistException - no such coupon
	 * @throws noSuchCartException  - cart not found
	 * @throws NoSuchCouponException  - coupon not found
	 */
	public Coupon purchaseCoupon(int coupId) throws CustomerDoesnotExistException, CouponOutOfStockException,
			PurchaseDuplicationException, couponExpiredException, CouponDoesnotExistException, NoSuchCouponException, noSuchCartException {

		Customer customer = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
		if (!couponRepo.existsById(coupId))
			throw new CouponDoesnotExistException();
		Coupon coupon = couponRepo.findById(coupId).orElseThrow(CouponDoesnotExistException::new);
		if (coupon.getAmount() == 0)
			throw new CouponOutOfStockException();
		if (customer.getCoupons().contains(coupon)) { 
			throw new PurchaseDuplicationException();
		}
		Calendar cal = Calendar.getInstance();

		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

 		if (coupon.getEndDate().isBefore(currentTime))
			throw new couponExpiredException();

		else
		 
			coupon.getCustomers().add(customer);
	 
		coupon.setAmount(coupon.getAmount() - 1); 
		couponRepo.save(coupon);
		double price = coupon.getPrice(customer);
		Company company = coupon.getCompany();
		company.setBalance(company.getBalance() + price);
		companyRepo.save(company);
		customer.setRevenue(customer.getRevenue() + price);
 		if (customer.getRevenue() >= 2000)
			customer.setPrime(true);
		customerRepo.save(customer);
		return coupon;

	}

	/**
	 * Order cancellation requires: retrieving the canceled coupon and modifying the
	 * coupon's company, the coupon's amount, company's balance, customer's shopping cart (the coupon is back in cart, after
	 * it was removed from there on purchasing), and the customer's revenue.
	 * 
	 * @param coupId - coupon id
	 * @throws CustomerDoesnotExistException-customer not found
	 * @throws CouponDoesnotExistException-coupon not found
	 * @throws CouponOutOfStockException - coupon is out of stock
	 * @throws PurchaseDuplicationException - customer already purchased the product
	 * @throws noSuchCartException - cart not found
	 * @throws NoSuchCouponException  - coupon not found
	 * @throws couponExpiredException - coupon expired
	 */
	public void cancelOrder(int coupId) throws CustomerDoesnotExistException, CouponDoesnotExistException, NoSuchCouponException, noSuchCartException, PurchaseDuplicationException, CouponOutOfStockException, couponExpiredException {
		Customer cust = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
		Coupon c = couponRepo.findById(coupId).orElseThrow(CouponDoesnotExistException::new);
		c.getCustomers().remove(cust); 

		customerRepo.save(cust); 
		c.setAmount(c.getAmount() + 1);
		couponRepo.save(c);
		Company comp = c.getCompany();
		double price = c.getPrice(cust);
		 
		comp.setBalance(comp.getBalance() - price);
 		companyRepo.save(comp);
		c.setPrice(price);

	}

	public List<Coupon> getCustomerCoupons() {

		return couponRepo.findCustomerCoupons(id);
	}

	public List<Coupon> getCustomerCouponsBYCategory(Category cat) {

		return couponRepo.findCustomerCouponsByCategory(id, cat);
	}

	public List<Coupon> getCustomerCouponsUpToPrice(double maxprice) {
		return (couponRepo.findCustomerCouponsByPrice(id, maxprice));

	}

	public Customer getCustomerDetails() throws CustomerDoesnotExistException {
	 
		return customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
	}

 
	public List<Coupon> getAllCoupons() {
		return couponRepo.findAll();
	}

	 /**
	  * A method to be used for a customer to buy one coupon out of all existing coupons.
	  * @param coupId - coupon id
	  * @return - returns coupon object
	  * @throws NoSuchCouponException- coupon not found
	  */
	public Coupon getOneCoupon(int coupId) throws NoSuchCouponException {
		return couponRepo.findById(coupId).orElseThrow(NoSuchCouponException::new);
	}

	/**
	 * If the customer owns a cart then the method simply returns it, otherwise a
	*/


	private Customer convertToEntity(CustomerDTO dt){
		Customer customer = new Customer();
 		customer.setUserName(dt.getUserName());
		customer.setPassword(dt.getPassword());
		return customer;
	}
	private CustomerDTO convertToDTO(Customer customer){
		CustomerDTO dt = new CustomerDTO();
 		dt.setUserName(customer.getUserName());
		dt.setPassword(customer.getPassword());
		return dt;
	}

	@Override
	public boolean login(String email, String password) throws loginException {
		return false;
	}
}

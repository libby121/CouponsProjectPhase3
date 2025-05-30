package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exceptions.*;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.CustomerDTO;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.CustomerRepository;
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
  public class CustomerServiceImpl implements CustomerService {

	private UUID id;

 	private final AuthenticationManager authManager;
 	private final JwtService jwtService;
 	private final PasswordEncoder passwordEncoder;
	private final CompanyRepository companyRepo;
	private final CouponRepository couponRepo;
	private final CustomerRepository customerRepo;

	public CustomerServiceImpl(AuthenticationManager authManager, JwtService jwtService, PasswordEncoder passwordEncoder, CompanyRepository companyRepo, CouponRepository couponRepo, CustomerRepository customerRepo) {
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.companyRepo = companyRepo;
		this.couponRepo = couponRepo;
		this.customerRepo = customerRepo;
	}

	public String verify(String username, String password){

		Authentication authentication =
				authManager.authenticate(new
						UsernamePasswordAuthenticationToken(username,
						password));

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


	public Coupon getOneCoupon(int coupId) throws NoSuchCouponException {
		return couponRepo.findById(coupId).orElseThrow(NoSuchCouponException::new);
	}

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

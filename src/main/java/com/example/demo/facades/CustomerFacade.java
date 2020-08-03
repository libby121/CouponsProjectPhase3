package com.example.demo.facades;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.demo.beans.Category;
import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.Customer;
import com.example.demo.beans.ShoppingCart;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponOutOfStockException;
import com.example.demo.exceptions.CouponsCategoreyException;
import com.example.demo.exceptions.CustomerDoesnotExistException;
import com.example.demo.exceptions.NoSuchCouponException;
import com.example.demo.exceptions.PurchaseDuplicationException;
import com.example.demo.exceptions.couponExpiredException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.maxPriceException;
import com.example.demo.exceptions.noSuchCartException;
import com.example.demo.login.ClientType;

/**
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

public class CustomerFacade extends Facade {

	private int id;
	private String cartId;

	@Override
	public boolean login(String email, String password)  {
		if (customerRepo.existsByEmailAndPassword(email, password)) {
			Customer c = customerRepo.findByEmailAndPassword(email, password);
			id = c.getId();
			return true;
		} return false;
	}

	/**
	 * Before activating the purchase, it is checked whether the buyer is a prime
	 * customer. if he is then the coupon's price is 5% lower. A customer becomes
	 * prime if his revenue to the companies get's to 2000 ILS, after every
	 * purchase it is checked whether the customer should become a prime customer.Company's
	 * balance and coupon's amount are also modified.
	 * 
	 * @param coupon
	 * @throws CustomerDoesnotExistException
	 * @throws CouponOutOfStockException
	 * @throws PurchaseDuplicationException
	 * @throws couponExpiredException
	 * @throws CouponDoesnotExistException
	 * @throws noSuchCartException 
	 * @throws NoSuchCouponException 
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
		if (coupon.getEndDate().before(new Date(cal.getTimeInMillis())))
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
		removeCouponFromCart(coupId);
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
	 * @param coupId
	 * @throws CustomerDoesnotExistException
	 * @throws CouponDoesnotExistException
	 * @throws CouponOutOfStockException 
	 * @throws PurchaseDuplicationException 
	 * @throws noSuchCartException 
	 * @throws NoSuchCouponException 
	 * @throws couponExpiredException 
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
		addToCart(coupId);
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
	  * @param coupId
	  * @return
	  * @throws NoSuchCouponException
	  */
	public Coupon getOneCoupon(int coupId) throws NoSuchCouponException {
		return couponRepo.findById(coupId).orElseThrow(NoSuchCouponException::new);
	}

	/**
	 * If the customer owns a cart then the method simply returns it, otherwise a
	 * new Shopping cart is created, it's Id is assigned to the global cartId variable and the new Shopping cart is returned.
	 * @return
	 * @throws CustomerDoesnotExistException
	 */
	public ShoppingCart getOrCreateCart() throws CustomerDoesnotExistException {
		Customer c = customerRepo.findById(this.id).orElseThrow(CustomerDoesnotExistException::new);

		if (cartRepo.getByCustomerId(id) != null) {
			cartId = cartRepo.getByCustomerId(id).getCartId();
			return cartRepo.getByCustomerId(id);
		}
	 
		else {
			ShoppingCart cart = new ShoppingCart(c);
			cartRepo.save(cart);

			cartId = cart.getCartId();
			  

			return cart;
		}
	}

	/**
	 * A coupon can only be added to cart if it's amount and date are valid and if 
	 * it was not already purchased by the customer.
	 * @param couponId
	 * @return
	 * @throws NoSuchCouponException
	 * @throws noSuchCartException
	 * @throws PurchaseDuplicationException
	 * @throws CouponOutOfStockException
	 * @throws CustomerDoesnotExistException
	 * @throws couponExpiredException 
	 */
	public void addToCart(int couponId) throws NoSuchCouponException, noSuchCartException,
			PurchaseDuplicationException, CouponOutOfStockException, CustomerDoesnotExistException, couponExpiredException {
		ShoppingCart cart = 	getOrCreateCart();
 		Calendar cal = Calendar.getInstance(); 

	
		Coupon c = couponRepo.findById(couponId).orElseThrow(NoSuchCouponException::new);
		if (c.getEndDate().before(new Date(cal.getTimeInMillis())))
			throw new couponExpiredException();
		 
		if ( (getCustomerCoupons().contains(c)) || ( cart.getCoupons().contains(c)))
			throw new PurchaseDuplicationException();
		else if (c.getAmount() >= 1) {

 			 
			cart.getCoupons().add(c);
		 
			cartRepo.save(cart);
			 return;
		}
		else throw new CouponOutOfStockException();
	}

	
	public void removeCouponFromCart(int couponId) throws NoSuchCouponException, noSuchCartException {
		ShoppingCart cart = cartRepo.findById(cartId).orElseThrow(noSuchCartException::new);
		Coupon c = couponRepo.findById(couponId).orElseThrow(NoSuchCouponException::new);
		cart.getCoupons().remove(c);
		cartRepo.save(cart);
		 
	}

 
	public void deleteCart() throws noSuchCartException {
 
			cartRepo.deleteById(cartId);
		}
	



}

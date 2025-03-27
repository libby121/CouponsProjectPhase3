package com.example.demo.web;

import com.example.demo.beans.Category;
import com.example.demo.beans.Coupon;
import com.example.demo.exceptions.*;
import com.example.demo.facades.CustomerFacade;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

	 Map<String, SessionInfo> sessions;

	public CustomerController(Map<String, SessionInfo> sessions) {
		super();
		this.sessions = sessions;
	}

	@PostMapping("/purchase/{token}/{coupId}")
	public ResponseEntity<?> purchase( @PathVariable String token,@PathVariable int coupId)
			throws CustomerDoesnotExistException {
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
		 

			try {
				
				return ResponseEntity.ok(customer.purchaseCoupon(coupId));
			} catch (CouponOutOfStockException | PurchaseDuplicationException | couponExpiredException
					| CouponDoesnotExistException | com.example.demo.exceptions.CustomerDoesnotExistException | NoSuchCouponException | noSuchCartException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
	 
	}

	@DeleteMapping("/cancel/{token}/{couponId}") 
	public ResponseEntity<?> cancelOrder( @PathVariable String token,@PathVariable int couponId)
			throws CustomerDoesnotExistException {
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
		 
			try {
				customer.cancelOrder(couponId);
				return ResponseEntity.ok("coupon purchase was canceled");
			} catch (CouponDoesnotExistException | CustomerDoesnotExistException | NoSuchCouponException | noSuchCartException | PurchaseDuplicationException | CouponOutOfStockException | couponExpiredException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
	 
	}

@GetMapping("/allMyCoupons/{token}")
	public ResponseEntity<?> getAllCoupons(@PathVariable String token) {
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
	 


			return ResponseEntity.ok(customer.getCustomerCoupons());
	 
	}
	
	
	
	@GetMapping("/all/{token}")
	public ResponseEntity<?> getCustomerCoupons(@PathVariable String token) {
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
		 

			return ResponseEntity.ok(customer.getAllCoupons());
	 
	}


	@GetMapping("/category/{token}/{category}")
	public ResponseEntity<?> getCustomerCoupons( @PathVariable String token,@PathVariable Category category) { 
																												 
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
		 
			return ResponseEntity.ok(customer.getCustomerCouponsBYCategory(category));
		 
	}

	@GetMapping("/maxPrice/{token}/{price}")
	public ResponseEntity<?> getCustomerCoupons( @PathVariable String token,@PathVariable double price) {
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
			 

			return ResponseEntity.ok(customer.getCustomerCouponsUpToPrice(price));
		 
	}

	
	@GetMapping("/oneCoupon/{token}/{coupId}")
	public ResponseEntity<?>getOneCoupon( @PathVariable String token,@PathVariable int coupId){
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
		 
			try {
				return ResponseEntity.ok(customer.getOneCoupon(coupId));
			} catch (NoSuchCouponException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}
	 
	}
	
	
	@GetMapping("/{token}")
	public ResponseEntity<?> getDetails(@PathVariable String token) throws CustomerDoesnotExistException {
		SessionInfo session = sessions.get(token);
	 
			CustomerFacade customer = (CustomerFacade) session.getFacade();
	 
 			return ResponseEntity.ok(customer.getCustomerDetails());

	 
	}
	
	@GetMapping("/cart/{token}")
	public ResponseEntity<?>getOrCreateCart(@PathVariable String token) throws CustomerDoesnotExistException{
		SessionInfo session=sessions.get(token);
		CustomerFacade customer=(CustomerFacade)session.getFacade();
		return ResponseEntity.ok(customer.getOrCreateCart());
	}
	
	@PutMapping("/addToCart/{token}/{couponId}")
	public ResponseEntity<?>addToCart(@PathVariable String token,@PathVariable int couponId){
		SessionInfo session=sessions.get(token);
		CustomerFacade customer=(CustomerFacade)session.getFacade();
		 			try {
		 				customer.addToCart(couponId);
						return ResponseEntity.ok("coupon added to cart");
					} catch (NoSuchCouponException | noSuchCartException | PurchaseDuplicationException
							| CouponOutOfStockException | CustomerDoesnotExistException | couponExpiredException e) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
					}
		
		 
	}
	
	@DeleteMapping("/removeFromCart/{token}/{couponId}")
	public ResponseEntity<?>removeFromCart(@PathVariable String token,@PathVariable int couponId){
		 
			SessionInfo session=sessions.get(token);
			CustomerFacade customer=(CustomerFacade)session.getFacade();
			try {
				customer.removeCouponFromCart(couponId);
				return ResponseEntity.ok("coupon was successfully deleted  from cart");
			} catch (NoSuchCouponException | noSuchCartException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		 
	}
	

	@DeleteMapping("/deleteCart/{token}")
	public ResponseEntity<?>deleteCart(@PathVariable String token){
		 
			SessionInfo session=sessions.get(token);
			CustomerFacade customer=(CustomerFacade)session.getFacade();
			try {
				customer.deleteCart();
				return ResponseEntity.ok("cart was successfully deleted");
			} catch ( noSuchCartException |EmptyResultDataAccessException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		 
	}
	
	
	@GetMapping("/getImage/{token}/{coupId}")
	public ResponseEntity<?> getImage(@PathVariable String token, @PathVariable int coupId) {
		SessionInfo thisSession = sessions.get(token);

		CustomerFacade customer = (CustomerFacade) thisSession.getFacade();

		Coupon c ;

		try {
			c = customer.getOneCoupon(coupId);
 		} catch ( NoSuchCouponException e1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e1.getMessage());
		 			 
		}


		RandomAccessFile f=null;
		try {
			f = new RandomAccessFile("src\\main\\resources\\static\\assets\\images\\" + c.getImage(), "r");
		} catch (FileNotFoundException e) {
 			e.printStackTrace();
		}
		byte[] bytes=null;
		try {
			bytes = new byte[(int)f.length()];
			f.readFully(bytes);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());		}
		

	    // Set the headers of return type to be an image:
	    final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);

	    return new ResponseEntity<byte[]> (bytes, headers, HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

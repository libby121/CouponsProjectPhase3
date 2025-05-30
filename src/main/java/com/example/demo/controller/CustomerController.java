package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Coupon;
import com.example.demo.exceptions.*;
import com.example.demo.service.CustomerServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {


	private CustomerServiceImpl customerService;

	public CustomerController(CustomerServiceImpl customerService) {
		this.customerService = customerService;
	}


	@PostMapping ("/login/{username}/{password}")
	public ResponseEntity<?>login
			(@PathVariable("username") String username, @PathVariable String password){
		return ResponseEntity.ok(
				customerService.verify(username, password));
	}

	@PostMapping("/register/{username}/{password}/{email}")
	public ResponseEntity<?> register(@PathVariable String username,
									  @PathVariable String password,
									  @PathVariable String email
	){
		try{
			return ResponseEntity.ok(customerService.register(username,password,email));

		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("registration failed");
		}
	}

	@PostMapping("/purchase/{coupId}")
	public ResponseEntity<?> purchase(@PathVariable int coupId)
			throws CustomerDoesnotExistException {

			try {
				
				return ResponseEntity.ok(customerService.purchaseCoupon(coupId));
			} catch (CouponOutOfStockException | PurchaseDuplicationException | couponExpiredException
					| CouponDoesnotExistException | com.example.demo.exceptions.CustomerDoesnotExistException | NoSuchCouponException | noSuchCartException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
	 
	}

	@DeleteMapping("/cancel/{couponId}")
	public ResponseEntity<?> cancelOrder(@PathVariable int couponId)
			throws CustomerDoesnotExistException {

			try {
				customerService.cancelOrder(couponId);
				return ResponseEntity.ok("coupon purchase was canceled");
			} catch (CouponDoesnotExistException | CustomerDoesnotExistException | NoSuchCouponException | noSuchCartException | PurchaseDuplicationException | CouponOutOfStockException | couponExpiredException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
	 
	}

@GetMapping("/allMyCoupons")
	public ResponseEntity<?> getAllCoupons() {

 			return ResponseEntity.ok(customerService.getCustomerCoupons());

	}
	
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getCustomerCoupons() {

			return ResponseEntity.ok(customerService.getAllCoupons());
	 
	}


	@GetMapping("/category/{category}")
	public ResponseEntity<?> getCustomerCoupons(@PathVariable Category category) {

			return ResponseEntity.ok(customerService.getCustomerCouponsBYCategory(category));
		 
	}

	@GetMapping("/maxPrice/{price}")
	public ResponseEntity<?> getCustomerCoupons(@PathVariable double price) {

			return ResponseEntity.ok(customerService.getCustomerCouponsUpToPrice(price));
		 
	}

	
	@GetMapping("/oneCoupon/{coupId}")
	public ResponseEntity<?>getOneCoupon(@PathVariable int coupId){

			try {
				return ResponseEntity.ok(customerService.getOneCoupon(coupId));
			} catch (NoSuchCouponException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}
	 
	}
	
	
	@GetMapping("")
	public ResponseEntity<?> getDetails() throws CustomerDoesnotExistException {

 			return ResponseEntity.ok(customerService.getCustomerDetails());

	}

		 

	

	
	
	
	
	
}

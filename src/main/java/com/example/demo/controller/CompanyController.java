package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Coupon;
import com.example.demo.exceptions.*;
import com.example.demo.model.CouponDTO;
import com.example.demo.service.CompanyService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = { "http://localhost:4200" })

public class CompanyController {

	private final CompanyService companyService;

	public CompanyController(CompanyService companyService) {
	     this.companyService = companyService;

	}
//
//
//	@PostMapping ("/login/company/{username}/{password}")
//	public ResponseEntity<?>login
//	(@PathVariable("username") String username, @PathVariable String password){
//		return ResponseEntity.ok(
//				companyService.verify(username, password));
//	}



   	@PostMapping(value = "/add")
	public ResponseEntity<?> addCoupon( @RequestBody CouponDTO c

	) {
 	try {
			companyService.addCoupon(c);
			return ResponseEntity.ok(c);

		} catch (CouponExistsException | CouponDateSetException | CompanyDoesNotExistException
				| CouponOutOfStockException e) {

			 
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}

	}

	@GetMapping("/one/{coupId}")
	public ResponseEntity<?> getOneCoupon(@PathVariable int coupId) {

		try {

			return ResponseEntity.ok(companyService.getOneCoupon(coupId));
		} catch (CouponDoesnotExistException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCoupon( @RequestBody CouponDTO c) {

		try {
			companyService.updateCoupon(c);

			return ResponseEntity.ok(c);
		} catch (unchangeableCouponCompanyId | CouponDateSetException | CouponOutOfStockException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@DeleteMapping("/delete/{couponId}")
	public ResponseEntity<?> deleteCoupon(@PathVariable int couponId) {


		try {
			companyService.deleteCoupon(couponId);
			return ResponseEntity.ok("coupon deleted");
		} catch (EmptyResultDataAccessException | CompanyDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}


@PreAuthorize("hasRole('MANAGER')")
@GetMapping("/allCoupons")
	public ResponseEntity<?> getCompanyCoupons() {

		return ResponseEntity.ok(companyService.getCompanyCoupons());

	}

	@GetMapping("/category/{c}")
	public ResponseEntity<?> getByCategory(@PathVariable Category c) {

		return ResponseEntity.ok(companyService.getCouponsByCategory(c));
	}

	@GetMapping("/byPrice/{price}")
	public ResponseEntity<?> getByPrice( @PathVariable double price) {

		return ResponseEntity.ok(companyService.getCouponUpToMaxPrice(price));

	}

	@GetMapping("/")
	public ResponseEntity<?> getCompanyDetails() {

		try {
			return ResponseEntity.ok(companyService.getCompanyDetails());
		} catch (CompanyDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}

	}


}

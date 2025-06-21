package com.example.demo.controller;

import com.example.demo.entity.Company;
import com.example.demo.entity.Coupon;
import com.example.demo.entity.Customer;
import com.example.demo.exceptions.*;
import com.example.demo.model.CompanyDTO;
import com.example.demo.service.AdminService;
import com.example.demo.service.CompanyService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * RestController tag-A special version(of spring 4) of @Controller which simplifies the Restful Web services and combines together
 * controller and ResponseBody tags- ResponseEntity is the default return type. Default scope is Singleton.
 * RequestMapping tag-Used for mapping web requests to specific classes or methods.
 * CrossOrigin tag-By default requests that are sent from different origins i.e different domains, ports, or protocols
 * are being blocked by the security mechanism - CORS policy. 
 * When adding this spring annotation I can easily define which origin I do give permission to.
 * PathVariable tag-Used for method parameters which do not require the userName of the variable before them when sent to the server.
 * As opposed to @QueryParam which are passed as a key-value pair. This makes the URL shorter but in some cases might be less straight-forward. 
 * RequestBody tag-Used for parameters that will not be sent in the URI itself but in the request body data. Especially for
 * complex objects that are sent from client. 
 *
 *
 * @author ליבי
 *
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final AdminService adminService;
	private final CompanyService companyService;

	public AdminController(AdminService adminService, CompanyService companyService) {
		this.adminService = adminService;
        this.companyService = companyService;
    }


	@PutMapping("/updateCompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company)
			throws CompanyDoesNotExistException {

		try {
			adminService.updateCompany(company);
			return ResponseEntity.ok(company);
		} catch (unmodifiedCompanyNameException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@DeleteMapping("/deleteCompany")
	public ResponseEntity<?> deleteCompany(@PathVariable UUID companyId) {

		try {
			adminService.deleteCompany(companyId);
			return ResponseEntity.ok("company deleted");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("company not found..");
		}

	}

	@GetMapping("/allCompany")
	public ResponseEntity<?> getAllCompanies() {
 		return ResponseEntity.ok(adminService.getAllCompanies());

 
	}


	@GetMapping("/oneCompany/{companyId}")
	public ResponseEntity<?> getOneCompany(@PathVariable UUID companyId)
			throws CompanyDoesNotExistException {


		return ResponseEntity.ok(adminService.getCompanyByID(companyId));

	}



	@PutMapping("/updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer)
			throws CustomerDoesnotExistException, CompanyDoesNotExistException {

		adminService.updateCustomer(customer);
		return ResponseEntity.ok(customer);

	}

	@DeleteMapping("/deleteCustomer/{customerId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable UUID customerId) {

		try {
			adminService.deleteCustomer(customerId);
			return ResponseEntity.ok("customer deleted");
		} catch (EmptyResultDataAccessException | CustomerDoesnotExistException e) {// for the runTime exception of
																					// trying to delete a customer that
																					// does not exist.
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("customer was not found");
		}

	}

	@GetMapping("/allCustomer")
	public ResponseEntity<?> gatAllCustomers() {

		return ResponseEntity.ok(adminService.getAllCustomers());

	}

	@GetMapping("/oneCustomer/{customerId}")
	public ResponseEntity<?> getOneCustomer(@PathVariable UUID customerId)
			throws CustomerDoesnotExistException {

		return ResponseEntity.ok(adminService.getOneCustomer(customerId));

	}

	@GetMapping("/allCoupons")
	public ResponseEntity<?> getAllCoupons() {

		return ResponseEntity.ok(adminService.getAllCoupons());

	}
	

	
	

	
	
	
	
	
	
	
	
	
	
	
	
	

}

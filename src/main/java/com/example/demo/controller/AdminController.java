package com.example.demo.web;

import com.example.demo.entity.Company;
import com.example.demo.entity.Coupon;
import com.example.demo.entity.Customer;
import com.example.demo.exceptions.*;
import com.example.demo.service.AdminFacade;
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
import java.util.UUID;

/**
 * RestController tag-A special version(of spring 4) of @Controller which simplifies the Restful Web services and combines together
 * controller and ResponseBody tags- ResponseEntity is the default return type. Default scope is Singleton.
 * RequestMapping tag-Used for mapping web requests to specific classes or methods.
 * CrossOrigin tag-By default requests that are sent from different origins i.e different domains, ports, or protocols
 * are being blocked by the security mechanism - CORS policy. 
 * When adding this spring annotation I can easily define which origin I do give permission to.
 * PathVariable tag-Used for method parameters which do not require the name of the variable before them when sent to the server.
 * As opposed to @QueryParam which are passed as a key-value pair. This makes the URL shorter but in some cases might be less straight-forward. 
 * RequestBody tag-Used for parameters that will not be sent in the URI itself but in the request body data. Especially for
 * complex objects that are sent from client. 
 * 
 * 
 *
 * @author ליבי
 *
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

	 Map<String, SessionInfo>sessions;


	
	@PostMapping("/company/add/{token}")
	public  ResponseEntity<?> addCompany(@PathVariable String token,@RequestBody Company company) {
 		 
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();
 
		try {

			admin.addCompany(company);
		return ResponseEntity.ok(company);

		} catch (companyExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());}


 	}

	@PutMapping("/company/update/{token}")
	public ResponseEntity<?> updateCompany( @PathVariable String token,@RequestBody Company company)
			throws CompanyDoesNotExistException {
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();

		try {
			admin.updateCompany(company);
			return ResponseEntity.ok(company);
		} catch (unmodifiedCompanyNameException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@DeleteMapping("/company/delete/{token}/{companyId}")
	public ResponseEntity<?> deleteCompany( @PathVariable String token,@PathVariable UUID companyId) {
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();

		try {
			admin.deleteCompany(companyId);
			return ResponseEntity.ok("company deleted");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("company not found..");
		}

	}

	@GetMapping("/company/all/{token}")
	public ResponseEntity<?> getAllCompanies(@PathVariable String token) {
		SessionInfo session = sessions.get(token);
		 
		AdminFacade admin = (AdminFacade) session.getFacade();
		return ResponseEntity.ok(admin.getAllCompanies());

 
	}


	@GetMapping("/company/{token}/{companyId}")
	public ResponseEntity<?> getOneCompany(@PathVariable String token,@PathVariable UUID companyId)
			throws CompanyDoesNotExistException {

		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();

		return ResponseEntity.ok(admin.getCompanyByID(companyId));

	}

	@PostMapping("/customer/add/{token}")
	public ResponseEntity<?> addCustomer( @PathVariable String token,@RequestBody Customer customer) {
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();

		try {
			admin.addCustomer(customer);
			return ResponseEntity.ok(customer);
		} catch (CustomerExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@PutMapping("/customer/update/{token}")
	public ResponseEntity<?> updateCustomer( @PathVariable String token,@RequestBody Customer customer)
			throws CustomerDoesnotExistException, CompanyDoesNotExistException {
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();

		admin.updateCustomer(customer);
		return ResponseEntity.ok(customer);

	}

	@DeleteMapping("/customer/delete/{token}/{customerId}")
	public ResponseEntity<?> deleteCustomer( @PathVariable String token,@PathVariable int customerId) {
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();

		try {
			admin.deleteCustomer(customerId);
			return ResponseEntity.ok("customer deleted");
		} catch (EmptyResultDataAccessException | CustomerDoesnotExistException e) {// for the runTime exception of
																					// trying to delete a customer that
																					// does not exist.
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("customer was not found");
		}

	}

	@GetMapping("/customer/all/{token}")
	public ResponseEntity<?> gatAllCustomers(@PathVariable String token) {
		SessionInfo session = sessions.get(token);
		AdminFacade admin = (AdminFacade) session.getFacade();

		return ResponseEntity.ok(admin.getAllCustomers());

	}

	@GetMapping("/customer/{token}/{customerId}")
	public ResponseEntity<?> getOneCustomer( @PathVariable String token,@PathVariable int customerId)
			throws CustomerDoesnotExistException {
		SessionInfo session = sessions.get(token);
		AdminFacade admin = (AdminFacade) session.getFacade();

		return ResponseEntity.ok(admin.getOneCustomer(customerId));

	}

	@GetMapping("/allCoupons/{token}")
	public ResponseEntity<?> getAllCoupons(@PathVariable String token) {
		SessionInfo session = sessions.get(token);

		AdminFacade admin = (AdminFacade) session.getFacade();
 
		return ResponseEntity.ok(admin.getAllCoupons());

	}
	
	
	@GetMapping("/getCouponImage/{token}/{coupId}")
	public ResponseEntity<?> getImage(@PathVariable String token, @PathVariable int coupId) {
		SessionInfo thisSession = sessions.get(token);
 		AdminFacade admin = (AdminFacade) thisSession.getFacade();

		Coupon c = null;

		try {
			c = admin.getOneCoupon(coupId);
			 
		} catch (  CouponDoesnotExistException e1) {
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

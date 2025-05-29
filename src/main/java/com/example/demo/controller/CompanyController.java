package com.example.demo.web;

import com.example.demo.entity.Category;
import com.example.demo.entity.Coupon;
import com.example.demo.exceptions.*;
import com.example.demo.service.CompanyFacade;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = { "http://localhost:4200" })
public class CompanyController {

	 Map<String, SessionInfo> sessions;

	public CompanyController(Map<String, SessionInfo> sessions) {
 		this.sessions = sessions;
	}
	
	
	
	
	

	/**
	 * Uploading an image from desktop: The method parameter is a multipartFile,
	 * which enables me to retrieve the image bytes, name and other separate
	 * components of the original file. After getting the original file name, a new
	 * file is created under the project directory, on which the original file bytes are
	 * written to, byte by byte with the help of FileOutputStream.
	 * 
	 * @return
	 * @param token
	 * @param file
	 */
	@PostMapping(value = "/upload/{token}")

	public ResponseEntity<?> uploadImage(@PathVariable String token,

			@RequestParam("file") MultipartFile file)

	{
 
		SessionInfo thisSession = sessions.get(token);

		CompanyFacade company = (CompanyFacade) thisSession.getFacade();

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		String uploadDir = "src\\main\\resources\\static\\assets\\images";

		try (InputStream fileInput = file.getInputStream()) {

 			File fil = new File(uploadDir + "\\" + fileName);

 
			FileOutputStream write = new FileOutputStream(fil);

			write.write(file.getBytes());

			return ResponseEntity.ok("uploaded");

		} catch (IOException e) {
 			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}

	}

	@PostMapping(value = "/add/{token}")
	public ResponseEntity<?> addCoupon(@PathVariable String token, @RequestBody Coupon c

	) {

		SessionInfo thisSession = sessions.get(token);

		CompanyFacade company = (CompanyFacade) thisSession.getFacade();

		try {
			company.addCoupon(c);
			return ResponseEntity.ok(c);

		} catch (CouponExistsException | CouponDateSetException | CompanyDoesNotExistException
				| CouponOutOfStockException e) {

			 
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}

	}

	@GetMapping("/one/{token}/{coupId}")
	public ResponseEntity<?> getOneCoupon(@PathVariable String token, @PathVariable int coupId) {
		SessionInfo session = sessions.get(token);

		CompanyFacade company = (CompanyFacade) session.getFacade();

		try {

			return ResponseEntity.ok(company.getOneCoupon(coupId));
		} catch (CouponDoesnotExistException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/update/{token}")
	public ResponseEntity<?> updateCoupon(@PathVariable String token, @RequestBody Coupon c) {
		SessionInfo session = sessions.get(token);

		CompanyFacade company = (CompanyFacade) session.getFacade();

		try {
			company.updateCoupon(c);

			return ResponseEntity.ok(c);
		} catch (unchangeableCouponCompanyId | CouponDateSetException | CouponOutOfStockException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@DeleteMapping("/delete/{token}/{couponId}")
	public ResponseEntity<?> deleteCoupon(@PathVariable String token, @PathVariable int couponId) {
		SessionInfo currentSession = sessions.get(token);
		CompanyFacade company = (CompanyFacade) currentSession.getFacade();

		try {
			company.deleteCoupon(couponId);
			return ResponseEntity.ok("coupon deleted");
		} catch (EmptyResultDataAccessException | CompanyDoesNotExistException | CouponOfAnotherCompanyException
				| CouponDoesnotExistException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}
	


	@GetMapping("/allCoupons/{token}")
	public ResponseEntity<?> getCompanyCoupons(@PathVariable String token) {
		SessionInfo session = sessions.get(token);

		CompanyFacade company = (CompanyFacade) session.getFacade();

		return ResponseEntity.ok(company.getCompanyCoupons());

	}

	@GetMapping("/category/{token}/{c}")
	public ResponseEntity<?> getByCategory(@PathVariable String token, @PathVariable Category c) {
		SessionInfo session = sessions.get(token);

		CompanyFacade company = (CompanyFacade) session.getFacade();
		return ResponseEntity.ok(company.getCouponsByCategory(c));
	}

	@GetMapping("/price/{token}/{price}")
	public ResponseEntity<?> getByPrice(@PathVariable String token, @PathVariable double price) {
		SessionInfo session = sessions.get(token);
		CompanyFacade company = (CompanyFacade) session.getFacade();

		return ResponseEntity.ok(company.getCouponUpToMaxPrice(price));

	}

	@GetMapping("/{token}")
	public ResponseEntity<?> getCompanyDetails(@PathVariable String token) {
		SessionInfo session = sessions.get(token);

		CompanyFacade company = (CompanyFacade) session.getFacade();

		try {
			return ResponseEntity.ok(company.getCompanyDetails());
		} catch (CompanyDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}

	}
	@GetMapping("/getImage/{token}/{coupId}")
	public ResponseEntity<?> getImage(@PathVariable String token, @PathVariable int coupId) {
		SessionInfo thisSession = sessions.get(token);

		CompanyFacade company = (CompanyFacade) thisSession.getFacade();

		Coupon c = null;

		try {
			c = company.getOneCoupon(coupId);
 		} catch (CouponDoesnotExistException e1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e1.getMessage());
		}

		if(c.getImage()!=null) {

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
			 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("io exception");
		}
		

		 // Set the headers of return type to be an image:
	    final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);

	    return new ResponseEntity<byte[]> (bytes, headers, HttpStatus.CREATED);}
		else return ResponseEntity.ok(null);
	}
}

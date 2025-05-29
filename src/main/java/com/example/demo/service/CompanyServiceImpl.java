package com.example.demo.service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.example.demo.entity.*;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.example.demo.exceptions.CouponDateSetException;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponExistsException;
import com.example.demo.exceptions.CouponOfAnotherCompanyException;
import com.example.demo.exceptions.CouponOutOfStockException;
import com.example.demo.exceptions.CompanyDoesNotExistException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.unchangeableCouponCompanyId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.stereotype.Service
@Scope(value = "prototype")
public class CompanyMyService extends MyService {

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtService jwtService;
	@Autowired
	private PasswordEncoder passwordEncoder;//?

	private UUID id;
	private int idInt;


	/*
	the revised login method. used with authentication object and
	authenticationManager
	 */
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
	public CompanyDTO register(String username,String password, String email){
		Company c = new Company(username,password,email);
		c.setPassword(passwordEncoder.encode(c.getPassword()));
 		companyRepo.save(c);
		return convertToDTO(c);

	}
	public CompanyDTO register(CompanyDTO u){
		Company uu = convertToEntity(u);
		uu.setPassword(passwordEncoder.encode(uu.getPassword()));

		CompanyDTO uuu=convertToDTO(uu);
		companyRepo.save(uu);
		return uuu;

	}


	@Override
	public boolean login(String email, String password) throws loginException {

		Company c = companyRepo.findByEmailAndPassword(email, password).orElseThrow(loginException::new );
		id=c.getId();
		return (companyRepo.findByEmailAndPassword(email, password).isPresent());
		 

	}

	public Coupon getOneCoupon(int coupId) throws CouponDoesnotExistException {
		Coupon c = couponRepo.findById(coupId).orElse(null);
		if (couponRepo.findByCompanyId(id).contains(c))
			return c;
		else
			throw new CouponDoesnotExistException();
	}

	public List<Coupon> getCouponsByCategory(Category cat) { 
		return couponRepo.findByCompanyIdAndCategory(id, cat);
	}

	public void addCoupon(Coupon coup) throws CouponExistsException, CouponDateSetException,
			CompanyDoesNotExistException, CouponOutOfStockException {
		Calendar cal = Calendar.getInstance();
		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


		if (coup.getStartDate().getSecond() > (coup.getEndDate().getSecond()))
			throw new CouponDateSetException();
		if (coup.getStartDate().isAfter(coup.getEndDate()) || !((currentTime.isBefore(coup.getStartDate()))))
			throw new CouponDateSetException();
		if (coup.getAmount() <= 0)
			throw new CouponOutOfStockException();
		if (getCompanyCoupons() != null) {
			for (Coupon c : getCompanyCoupons()) {
				if (c.getTitle().equals(coup.getTitle()))
					throw new CouponExistsException();
			}

		}

 		coup.setCompany(companyRepo.findById(id).orElseThrow(CompanyDoesNotExistException::new));// check why i must
																									// have this..?
		coup.setSalePrice(false);
		couponRepo.save(coup);
	}

	public List<Coupon> getCompanyCoupons() {
		 
		return couponRepo.findByCompanyId(id);
	}

	public void updateCoupon(Coupon coupon)
			throws unchangeableCouponCompanyId, CouponDateSetException, CouponOutOfStockException {
		Calendar cal = Calendar.getInstance();
		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


		if (coupon.getStartDate().getSecond() > (coupon.getEndDate().getSecond()))
			throw new CouponDateSetException();
		if (coupon.getStartDate().isAfter(coupon.getEndDate()) || coupon.getStartDate().isBefore(currentTime))
			throw new CouponDateSetException();
		if (coupon.getAmount() <= 0)
			throw new CouponOutOfStockException();
		if (coupon.getCompany().getId().equals(id)) {
			couponRepo.save(coupon);
			coupon.getCompany().setLastUpdate((currentTime));
			companyRepo.save(coupon.getCompany());
		}

		else
			throw new unchangeableCouponCompanyId();
	}

	public void deleteCoupon(int Couponid)
			throws CompanyDoesNotExistException, CouponOfAnotherCompanyException, CouponDoesnotExistException {
		Calendar cal = Calendar.getInstance();

		Company comp = companyRepo.findById(id).orElseThrow(CompanyDoesNotExistException::new);
		Coupon coup = couponRepo.findById(Couponid).orElseThrow(CouponDoesnotExistException ::new);
		if (!comp.getCoupons().contains(coup))
			throw new CouponOfAnotherCompanyException();
		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		getCompanyDetails().setLastUpdate(currentTime);

		companyRepo.save(comp);

		couponRepo.deleteById(Couponid);


	}

	/**
	 * A method for reading the deleted coupons file, (which is set in the AOP
	 * class)
	 * 
	 * @throws IOException
	 */
	public void fileRead() throws IOException {
		try (FileReader reader = new FileReader("C:\\TestsF\\CouponsArchiveFile.txt")) {
			int tav = reader.read();

			while (tav != -1) {
				System.out.print((char) tav);
				tav = reader.read();
			}
		}
	}

	public List<Coupon> getCouponUpToMaxPrice(double maxPrice) {
		return couponRepo.findByCompanyIdAndPriceLessThanEqual(id, maxPrice);
	}

	public Company getCompanyDetails() throws CompanyDoesNotExistException {
		return companyRepo.findById(id).orElseThrow(CompanyDoesNotExistException::new);

	}

	private Company convertToEntity(CompanyDTO dt){
		Company company = new Company();
 		company.setUserName(dt.getUserName());
		company.setPassword(dt.getPassword());
		return company;
	}
	private CompanyDTO convertToDTO(Company company){
		CompanyDTO dt = new CompanyDTO();
 		dt.setUserName(company.getUserName());
		dt.setPassword(company.getPassword());
		dt.setEmail(company.getEmail());

		return dt;
	}
}

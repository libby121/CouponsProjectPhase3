package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.example.demo.entity.*;
 import com.example.demo.model.CompanyDTO;
import com.example.demo.model.CouponDTO;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.CustomerRepository;
import org.springframework.context.annotation.Scope;

import com.example.demo.exceptions.CouponDateSetException;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponExistsException;
import com.example.demo.exceptions.CouponOutOfStockException;
import com.example.demo.exceptions.CompanyDoesNotExistException;
import com.example.demo.exceptions.unchangeableCouponCompanyId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "prototype")
public class CompanyServiceImpl implements CompanyService {

	private String rawPassword ;
	private final CompanyRepository companyRepo;
	private final CouponRepository couponRepo;
	private final CustomerRepository customerRepo;

	private UUID companyId;


	public CompanyServiceImpl( CustomerRepository customerRepo, CouponRepository couponRepo, CompanyRepository companyRepo) {

 		this.customerRepo = customerRepo;
		this.couponRepo = couponRepo;
		this.companyRepo = companyRepo;
	}

//	public CompanyDTO register(CompanyDTO u){
//		Company uu = convertToEntity(u);
//		uu.setPassword(passwordEncoder.encode(uu.getPassword()));
//        //decode password?
//		CompanyDTO uuu=convertToDTO(uu);
//		companyRepo.save(uu);
//		return uuu;
//
//	}

	public Coupon getOneCoupon(int coupId) throws CouponDoesnotExistException {
		Coupon c = couponRepo.findById(coupId).orElse(null);
		if (couponRepo.findByCompanyId(companyId).contains(c))
			return c;
		else
			throw new CouponDoesnotExistException();
	}

	public List<Coupon> getCouponsByCategory(Category cat) { 
		return couponRepo.findByCompanyIdAndCategory(companyId, cat);
	}

	public void addCoupon(CouponDTO couponDT) throws CouponExistsException, CouponDateSetException,
			CompanyDoesNotExistException, CouponOutOfStockException {
		Calendar cal = Calendar.getInstance();
		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Coupon coup = convertToEntity(couponDT);
		if (coup.getStartDate().getSecond() > (coup.getEndDate().getSecond()))
			throw new CouponDateSetException();
		if (coup.getStartDate().isAfter(coup.getEndDate()) || !((currentTime.isBefore(coup.getStartDate()))))
			throw new CouponDateSetException();
		if (coup.getAmount() <= 0)
			throw new CouponOutOfStockException();
		if (getCompanyCoupons() != null) {
			for (CouponDTO c : getCompanyCoupons()) {
				if (c.getTitle().equals(coup.getTitle()))
					throw new CouponExistsException();
			}

		}

 		coup.setCompany(companyRepo.findById(companyId).orElseThrow(CompanyDoesNotExistException::new));// check why i must
																									// have this..?
		coup.setSalePrice(false);
		couponRepo.save(coup);
	}

	public List<CouponDTO> getCompanyCoupons() {

        List<CouponDTO> list = new ArrayList<>();
        for (Coupon coupon : couponRepo.findByCompanyId(companyId)) {
            CouponDTO couponDTO = convertToDTO(coupon);
            list.add(couponDTO);
        }
        return list;
	}

	public void updateCoupon(CouponDTO couponDT)
			throws unchangeableCouponCompanyId, CouponDateSetException, CouponOutOfStockException {
		Calendar cal = Calendar.getInstance();
		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		Coupon coupon = convertToEntity(couponDT);

		if (coupon.getStartDate().getSecond() > (coupon.getEndDate().getSecond()))
			throw new CouponDateSetException();
		if (coupon.getStartDate().isAfter(coupon.getEndDate()) || coupon.getStartDate().isBefore(currentTime))
			throw new CouponDateSetException();
		if (coupon.getAmount() <= 0)
			throw new CouponOutOfStockException();
		if (coupon.getCompany().getId().equals(companyId)) {
			couponRepo.save(coupon);
			coupon.getCompany().setLastUpdate((currentTime));
			companyRepo.save(coupon.getCompany());
		}

		else
			throw new unchangeableCouponCompanyId();
	}

	public void deleteCoupon(int couponid)
			throws CompanyDoesNotExistException {
		Calendar cal = Calendar.getInstance();

		Company comp = companyRepo.findById(companyId).orElseThrow(CompanyDoesNotExistException::new);
		Coupon coup = couponRepo.findById(couponid).orElseThrow();
		LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		getCompanyDetails().setLastUpdate(currentTime);

		companyRepo.save(comp);

		couponRepo.deleteById(couponid);


	}

	public List<Coupon> getCouponUpToMaxPrice(double maxPrice) {
		return couponRepo.findByCompanyIdAndPriceLessThanEqual(companyId, maxPrice);
	}

	public Company getCompanyDetails() throws CompanyDoesNotExistException {
		return companyRepo.findById(companyId).orElseThrow(CompanyDoesNotExistException::new);

	}

	private Coupon convertToEntity(CouponDTO dt){
		Coupon c = new Coupon();
		c.setCompany(dt.getCompany());
		c.setCategory(dt.getCategory());
		c.setAmount(dt.getAmount());
		c.setDescription(dt.getDescription());
		c.setImage(dt.getImage());
		c.setPrice(dt.getPrice());
		return c;

	}
	private CouponDTO convertToDTO(Coupon c){
		 CouponDTO dt =new CouponDTO();
		 dt.setAmount(c.getAmount());
		 dt.setCategory(c.getCategory());
		 dt.setId(c.getId());
		 dt.setTitle(c.getTitle());
		 dt.setPrice(c.getPrice());
		 dt.setSalePrice(c.isSalePrice());
		 dt.setDescription(c.getDescription());
		 dt.setStartDate(c.getStartDate());
		 dt.setEndDate(c.getEndDate());
		 return dt;
	}
	private Company convertToEntity(CompanyDTO dt){
		Company company = new Company();
 		company.setUserName(dt.getUserName());
		company.setPassword(dt.getPassword());
// 		company.setRoles(dt.getRoles());
 		company.setEmail(dt.getEmail());
		return company;
	}
	private CompanyDTO convertToDTO(Company company){
		CompanyDTO dt = new CompanyDTO();
		dt.setBalance(company.getBalance());
		dt.setLastUpdate(company.getLastUpdate());
		dt.setUserName(company.getUserName());
 		dt.setEmail(company.getEmail());
        dt.setCoupons(company.getCoupons());
		dt.setRoles(company.getRoles());
		return dt;
	}
}

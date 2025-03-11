package com.example.demo.facades;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.demo.beans.Category;
import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.exceptions.CouponDateSetException;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponExistsException;
import com.example.demo.exceptions.CouponOfAnotherCompanyException;
import com.example.demo.exceptions.CouponOutOfStockException;
import com.example.demo.exceptions.CompanyDoesNotExistException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.maxPriceException;
import com.example.demo.exceptions.unchangeableCouponCompanyId;
import com.example.demo.login.ClientType;

@Service
@Scope(value = "prototype")
public class CompanyFacade extends Facade {

	private int id;

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
		if (coupon.getCompany().getId() == id) {
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
		couponRepo.deleteCouponFromCart(Couponid);

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

}

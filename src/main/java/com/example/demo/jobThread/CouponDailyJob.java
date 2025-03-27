package com.example.demo.jobThread;

import com.example.demo.beans.Coupon;
import com.example.demo.db.CouponRepository;
import com.example.demo.exceptions.CompanyDoesNotExistException;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponOfAnotherCompanyException;
import com.example.demo.facades.CompanyFacade;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

/**
 * ExpriationDailyJob thread with extra features. The thread eliminates coupons
 * after comparing their endDate with the current time. The comparison requires
 * the conversion from Calendar of java.util to SQL.
 * I added an extra loop to check about the coupon's amount. a discount of 20%
 * off is announced when the amount is between 10-100. An after-sale coupon is
 * marked, and so each coupon will get a discount only once.
 * 
 * @author ליבי
 *
 */

@Component
public class CouponDailyJob extends Thread {

	public CouponDailyJob(CouponRepository coupoRepo, CompanyFacade compafacade) {
		super();
		this.coupoRepo = coupoRepo;
		this.compafacade = compafacade;
	}

	private final CouponRepository coupoRepo;

	private boolean quit;

	private final CompanyFacade compafacade;

	public void run() {
		while (!quit) {

			for (Coupon coup : coupoRepo.findAll()) {
				Calendar cal = Calendar.getInstance();
				LocalDateTime currentTime = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


				if ((coup.getEndDate()).isBefore((currentTime))) {

					try {
						compafacade.deleteCoupon(coup.getId());
					} catch (NullPointerException | CompanyDoesNotExistException | CouponOfAnotherCompanyException |
                             CouponDoesnotExistException ignored) {

					}

				}

				else if ((coup.getAmount() <= 100 && coup.getAmount() > 10) && (!coup.isSalePrice())) {

					coup.setPrice(coup.getPrice() * 0.8);

					coup.setSalePrice(true);
					coupoRepo.save(coup);
					System.out
					.println("**limited time sale** 20% off on coupon number "
							+ coup.getId()+" ** "+coup.getTitle()+"**  of company: "+coup.getCompany().getId());
				}

			}
			try {
				Thread.sleep(86_400_000);
			} catch (InterruptedException e1) {

			}
		}

	}

	public void JobStop() {
		quit = true;
		interrupt();
	}

}

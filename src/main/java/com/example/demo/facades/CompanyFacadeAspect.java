package com.example.demo.facades;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP(Aspect Oriented Programming) allows adding logic and functionalities, to be used in distinct classes across the application,
 * on multiple areas, classes and methods to the existing code, without changing or modifying it. 
 *
 * 
 * @Aspect annotation defines a class as an AOP configuration. 
 * The class is also annotated as @Component to mark it for spring initial scanning (alternatively it can be @Configuration).
 *
 * I used it for writing to file after every coupon deletion. In this case this method is called twice, on main test and on the 
 * dailyJob thread. Instead of writing to file after every occurrence of the method, it is defined in here separately.
 * 
 */
@Component 
@Aspect
public class CompanyFacadeAspect {

	 
 
	@After("execution(* com.example.demo.facades.CompanyFacade.deleteCoupon(int) )&& args(couponId,..)")
	public void fileDeletion(JoinPoint joinPoint, int couponId) throws IOException {
		  
		try (FileWriter writer = new FileWriter("CouponsArchiveFile.txt", true)) { 
			Calendar cal = Calendar.getInstance();// true=append. add to existing data
			writer.write("\ncoupon " + couponId + " was deleted on " + cal.getTime());
 
		}
	}

}

 
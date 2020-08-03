package com.example.demo;

 import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.jobThread.CouponDailyJob;
/**
 * Spring context is responsible for the instantiation and configuration of and classes 
 * (marked throughout the meta-code in annotations, xml or other files).
 * By using the getBean() method an instantiated class is retrieved from context container.
 *
 * @author ליבי
 *
 */
@SpringBootApplication
public class CouponsProject3Application {

	public static void main(String[] args) {
  		ConfigurableApplicationContext ctx = SpringApplication.run(CouponsProject3Application.class, args);
		CouponDailyJob couponThread = ctx.getBean(CouponDailyJob.class);
		couponThread.start();

	}

}
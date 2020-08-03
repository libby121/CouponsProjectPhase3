package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.jobThread.CouponDailyJob;

public class Test {

	@Autowired
	private CouponDailyJob couponThread;
	
//	public void test() {
//		couponThread.start();
//	}
}

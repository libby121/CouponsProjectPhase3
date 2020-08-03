package com.example.demo.exceptions;

public class CouponOfAnotherCompanyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  CouponOfAnotherCompanyException () {
		super("you are trying to update or delete a coupon of another company");
	}
}

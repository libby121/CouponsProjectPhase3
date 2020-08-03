package com.example.demo.exceptions;

public class CouponsCategoreyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CouponsCategoreyException() {
		super("oops..coupons category was not found");
	}

}

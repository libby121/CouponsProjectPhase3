package com.example.demo.exceptions;

public class NoSuchCouponException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoSuchCouponException() {
		super("coupon was not found in dataBase");
	}

}

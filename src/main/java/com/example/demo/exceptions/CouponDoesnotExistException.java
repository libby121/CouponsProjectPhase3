package com.example.demo.exceptions;

public class CouponDoesnotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CouponDoesnotExistException() {
		super("oops..coupon does not exist");
		
	}

}

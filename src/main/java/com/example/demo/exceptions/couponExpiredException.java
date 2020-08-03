package com.example.demo.exceptions;

public class couponExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public couponExpiredException() {
		super("oops..coupon has expired");
	}
}

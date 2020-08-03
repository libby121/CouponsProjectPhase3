package com.example.demo.exceptions;

public class CouponOutOfStockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CouponOutOfStockException() {
		super("seems like this coupon is currently out of stock");
	}

}

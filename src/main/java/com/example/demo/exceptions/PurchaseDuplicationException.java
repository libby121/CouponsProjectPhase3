package com.example.demo.exceptions;

public class PurchaseDuplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PurchaseDuplicationException() {
		super("This coupon can only be purchased once");
	}
}

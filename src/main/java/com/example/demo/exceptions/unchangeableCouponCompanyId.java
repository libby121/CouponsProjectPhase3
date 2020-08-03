package com.example.demo.exceptions;

public class unchangeableCouponCompanyId extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public unchangeableCouponCompanyId() {
		super("coupon's company Id cannot be modified");
	}
}

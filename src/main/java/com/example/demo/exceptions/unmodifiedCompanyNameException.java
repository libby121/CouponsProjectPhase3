package com.example.demo.exceptions;

public class unmodifiedCompanyNameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public unmodifiedCompanyNameException() {
		super("company name cannot be altered");
	}
}

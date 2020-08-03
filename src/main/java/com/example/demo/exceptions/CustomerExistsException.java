package com.example.demo.exceptions;

public class CustomerExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomerExistsException() {
		super("customer already exists in dataBase");
	}

}

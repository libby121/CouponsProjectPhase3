package com.example.demo.exceptions;

public class companyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public companyExistsException () {
		super("company already exists in dataBase");
	}
}

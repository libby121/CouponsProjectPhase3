package com.example.demo.exceptions;

public class companyDoesntExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public companyDoesntExistException() {
		super("oops! company does not exist, please choose another id");
	}
}

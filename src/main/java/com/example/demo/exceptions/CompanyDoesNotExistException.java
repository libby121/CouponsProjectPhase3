package com.example.demo.exceptions;

public class CompanyDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompanyDoesNotExistException() {
		super("oops! company does not exist, please choose another id");
	}
}

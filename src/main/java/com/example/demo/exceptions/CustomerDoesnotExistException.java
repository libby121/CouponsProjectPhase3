package com.example.demo.exceptions;

public class CustomerDoesnotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CustomerDoesnotExistException() {
		super("oops..customer was not found by this id");
	}

}

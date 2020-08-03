package com.example.demo.exceptions;

public class loginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public loginException() {
		super("oops! something went wrong with login process, please check again email and password");
	
	}
}

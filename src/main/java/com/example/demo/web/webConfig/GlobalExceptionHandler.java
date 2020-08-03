package com.example.demo.web.webConfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.exceptions.CustomerDoesnotExistException;
import com.example.demo.exceptions.CompanyDoesNotExistException;

/**
 * A global exception handler. Works automatically as an AOP code. 
 * Catches and handles all the occurrences of the below exceptions throughout the controllers classes.
 * Enables handling them all in one place.
 * 
 * @author ליבי
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	
	@ExceptionHandler(CustomerDoesnotExistException.class)
	public ResponseEntity<String>CustomerDoesNotExistHandler(CustomerDoesnotExistException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	
	@ExceptionHandler(CompanyDoesNotExistException.class)
	public ResponseEntity<String>CompanyDoesNotExistHandler(CompanyDoesNotExistException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}


package com.example.demo.facades;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.db.CompanyRepository;
import com.example.demo.db.CouponRepository;
import com.example.demo.db.CustomerRepository;
import com.example.demo.db.ShoppingCartRepo;
import com.example.demo.exceptions.loginException;

/**
 * Generic class, of which the other facades (services) inherit the repository
 * fields and the login method. Creating this generic class helps organizing the
 * code, makes it reusable and flexible. At run time the concrete facade will be
 * used.
 * 
 * The three other facades will be marked as facades by service annotation.
 * 
 * @Service is a specific @component(=>signals classes to be managed by spring
 *          context with the functionality of dependency injection). a facade
 *          class is usually marked as @service-a business-logic class, instead
 *          of @component.
 * 
 *          Each of them facades will be of 'prototype' scope, since the returned
 *          facade depends upon the id field  (which is initialized after a
 *          successful login process).
 *          Except for AdminFacade, A client will get a particular facade and
 *          not a 'universal' facade (i.e particular values will be returned
 *          from the methods, based on his own state and his own saved
 *          data).
 * 
 * 
 */

public abstract class Facade {
	@Autowired
	protected CompanyRepository companyRepo;// must be protected at least
	@Autowired
	protected CouponRepository couponRepo;
	@Autowired
	protected CustomerRepository customerRepo;
	@Autowired
	ShoppingCartRepo cartRepo;


	public abstract boolean login(String email, String password) throws loginException  ;
}

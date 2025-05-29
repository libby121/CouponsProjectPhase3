 package com.example.demo.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.beans.Customer;

/**
 * Repositories interfaces are used for accessing (and persisting) database information. 
 * The three repositories extend JPARepository which enables basic CRUD operations.
 * @author ליבי
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	boolean existsByEmailAndPassword(String email, String password);
	
	Customer findByEmailAndPassword(String email, String password);

	

}

 package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

 /**
 * Repositories interfaces are used for accessing (and persisting) database information. 
 * The three repositories extend JPARepository which enables basic CRUD operations.
 * @author ליבי
 *
 */
 @Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	boolean existsByEmailAndPassword(String email, String password);
	
	Customer findByEmailAndPassword(String email, String password);


	Optional<Customer> findByEmail(String email);
}

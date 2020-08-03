package com.example.demo.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.beans.Coupon;
import com.example.demo.beans.Category;
import com.example.demo.beans.Company;

/**
 * Repositories interfaces are used for accessing (and persisting) database information. 
 * The three repositories extend JPARepository which enables basic CRUD operations.
 * @author ליבי
 *
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {



	boolean existsByEmailAndPassword(String email, String password);

	Optional<Company> findByEmailAndPassword(String email, String password);



	/**
	 * @Query-enables me to define my own flexible query(written in JPQL or SQL).
	 * I used it in update since I wanted only certain properties to be updated.
	 * 
	 * SpEl-Spring Expression Language. Supports querying and manipulating an object at runtime.
	 * I used it in order to easily access specific properties of the company object I get as a parameter.
	 * (for example, "update Company c set c.email=:comp.email.." won't work.Insted, SpEl special syntax allows me to 
	 * access the object property and referencing it by using # ).
	 * 
	 * @Modifying and @Transactional- used in custom query that manipulates and changes the data,
	 * instead of solely selecting and getting it .And for functions that require the JOIN clause behind the scenes.
	 * 
	 * @param comp
	 */
	@Transactional
	@Modifying  
	@Query("update Company c set c.email=:#{#comp.email}, c.password=:#{#comp.password},"
			+ "c.balance=:#{#comp.balance} where c.id=:#{#comp.id}")
	void updateCompany(@Param("comp") Company comp);// update here has to be save?
	 
 
	@Query("SELECT c from Coupon c where c.company=:company and c.category=:category order by c.title")
	List<Coupon> getCompanyCouponsByCategory(@Param("company") Company company, @Param("category") Category cat);//needed?

 
}

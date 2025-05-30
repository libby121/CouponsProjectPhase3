package com.example.demo.repository;


import com.example.demo.entity.Category;
import com.example.demo.entity.Coupon;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositories interfaces are used for accessing (and persisting) database
 * information. The three repositories extend JPARepository which enables basic
 * CRUD operations.
 * 
 * @author ליבי
 *
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	List<Coupon> findByCompanyIdAndCategory(UUID companyId, Category category);// works with category enum?

	List<Coupon> findByCompanyId(UUID companyId);

	List<Coupon> findByCompanyIdAndPriceLessThanEqual(UUID companyId, double price);

	/**
	 * Requires JOIN operation since the customer_id is not referenced in the
	 * coupons table, it has to be taken from the @ManyToMany coupons purchases
	 * table.
	 * 
	 * @param customerId- customer id
	 * @return Coupon list
	 */
	@Query(value = "select * from coupons join customers_vs_coupons ON coupons.id=customers_vs_coupons.coupon_id"
			+ " where customer_id=:customerId", nativeQuery = true)
	List<Coupon> findCustomerCoupons(UUID customerId);

	@Query(value = "select * from coupons join customers_vs_coupons ON coupons.id=customers_vs_coupons.coupon_id "
			+ "where customer_id=:customerId and category=:#{#category.ordinal()}", nativeQuery = true)
	List<Coupon> findCustomerCouponsByCategory(UUID customerId, Category category);

	@Query(value = "select * from coupons join customers_vs_coupons ON coupons.id=customers_vs_coupons.coupon_id "
			+ "where customer_id=:customerId and price<=:maxPrice", nativeQuery = true)
	List<Coupon> findCustomerCouponsByPrice(UUID customerId, double maxPrice);

	/**
	 * Coupon purchases history deletion. Will be needed in deleteCompany() and
	 * deleteCustomer() methods.
	 * 
	 * @param custId- customer id
	 * @param coupId - coupon id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from customers_vs_coupons where customer_id=:custId and coupon_id=:coupId", nativeQuery = true)
	void deleteCouponPurchase(UUID custId, int coupId);
	
	Optional<Coupon>findById(int copuonId);


 }

package com.example.demo.db;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.beans.Category;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.Customer;

/**
 * Repositories interfaces are used for accessing (and persisting) database
 * information. The three repositories extend JPARepository which enables basic
 * CRUD operations.
 * 
 * @author ליבי
 *
 */
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	List<Coupon> findByCompanyIdAndCategory(int companyId, Category category);// works with category enum?

	List<Coupon> findByCompanyId(int companyId);

	List<Coupon> findByCompanyIdAndPriceLessThanEqual(int companyId, double price);

	/**
	 * Requires JOIN operation since the customer_id is not referenced in the
	 * coupons table, it has to be taken from the @ManyToMany coupons purchases
	 * table.
	 * 
	 * @param customerID
	 * @return
	 */
	@Query(value = "select * from coupons join customers_vs_coupons ON coupons.id=customers_vs_coupons.coupon_id"
			+ " where customer_id=:customerId", nativeQuery = true)
	List<Coupon> findCustomerCoupons(int customerId);

	@Query(value = "select * from coupons join customers_vs_coupons ON coupons.id=customers_vs_coupons.coupon_id "
			+ "where customer_id=:customerId and category=:#{#category.ordinal()}", nativeQuery = true)
	List<Coupon> findCustomerCouponsByCategory(int customerId, Category category);

	@Query(value = "select * from coupons join customers_vs_coupons ON coupons.id=customers_vs_coupons.coupon_id "
			+ "where customer_id=:customerId and price<=:maxPrice", nativeQuery = true)
	List<Coupon> findCustomerCouponsByPrice(int customerId, double maxPrice);

	/**
	 * Coupon purchases history deletion. Will be needed in deleteCompany() and
	 * deleteCustomer() methods.
	 * 
	 * @param custId
	 * @param coupId
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from customers_vs_coupons where customer_id=:custId and coupon_id=:coupId", nativeQuery = true)
	void deleteCouponPurchase(int custId, int coupId);



	
	@Modifying
	@Transactional
	@Query(value="delete from shopping_carts_vs_coupons where coupon_id=:coupId",nativeQuery=true)
	void deleteCouponFromCart( int coupId);
	
		
		
		
		
		
}
	
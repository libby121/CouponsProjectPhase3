package com.example.demo.db;


import java.util.Set;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.beans.Coupon;
import com.example.demo.beans.ShoppingCart;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, String> {
	
	@Query(value="select * from coupons JOIN shopping_carts_vs_coupons on coupons.id=shopping_carts_vs_coupons.coupon_id where "
			+ "cart_id=:cartId", nativeQuery=true)
	public Set<Coupon>getCartItems(String cartId);

	
	@Query(value="select * from shopping_cart JOIN customers on shopping_cart.customer_id=customers.id where "
			+ "customer_id=:customerId",nativeQuery=true)
	 ShoppingCart getByCustomerId(int customerId);
		

	@Modifying
	@Transactional
	@Query(value="delete from shopping_cart where customer_id=:customerId",nativeQuery=true)
	 void  deleteCustomerCart(int customerId);
	
	@Modifying
	@Transactional
	@Query(value="delete from shopping_cart where id=:cartId",nativeQuery=true)
	 void  deleteCart(String cartId);
}
	
	
 
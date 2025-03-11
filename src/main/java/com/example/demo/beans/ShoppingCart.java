package com.example.demo.beans;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * An additional entity of a customer Shopping cart to be mapped to the
 * dataBase. Shopping cart is saved in dataBase instead of merely being saved in
 * client side, so that a customer has access to it no matter if he is not using
 * his PC, and if different customers use the same PC they can still access
 * their own cart only . That way the cart cannot be mistakenly deleted from
 * local storage, can be traced by the company and can be easily managed.
 * 
 * A Shopping cart contains a Set of coupons (as every coupon can only be
 * purchased once by each customer, so it can only appear in the cart uniquely
 * once).Shopping cart-coupon => @ManyToMany relationship since every coupon can
 * appear in different carts and every cart aims to have many different coupons.
 * 
 * Each cart is connected to one particular customer, and vice versa, a customer
 * can only own one cart at a time=> @OneToOne relationship.
 * 
 *           The cart Id is a unique UUID String and is generated automatically
 *           on cart creation.
 * 
 * @author ליבי
 *
 */

@Entity
public class ShoppingCart {

	@Id
	private String Id;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "shoppingCarts_vs_coupons", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "coupon_id"))
	private Set<Coupon> coupons;

	@OneToOne
	private Customer customer;

	 

	public ShoppingCart(Customer customer) {
		super();
		this.Id = String.valueOf(UUID.randomUUID());
		this.customer = customer;
		this.coupons=new HashSet<Coupon>();
		 
		 
 	}

	public ShoppingCart() {
		super();
	}

	public String getCartId() {
		return Id;
	}

	public void setCartId(String cartId) {
		this.Id = cartId;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	@Override
	public String toString() {
		return "ShoppingCart [Id=" + Id + ", coupons=" + coupons +   "]";
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}

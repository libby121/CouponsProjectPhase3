package com.example.demo.beans;

import java.io.Serializable;
import java.util.Set;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="customers")
public class Customer  implements Serializable {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
 	private int id;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column 
	private String email;

	@Column
	private String password;
	/**
	 * A prime customer gets a 5% off discount.
	 */
	@Column(name="is_Prime")
	private boolean isPrime;
	
	/**
	 * Customer' total revenue to the companies. 
	 */
	 @Column(scale=2)  
	private double revenue;
	/**
	 * Set (instead of a List) makes sure that both coupn_id and customer_id are the primary key together, 
	 * a Set in Java only accepts a unique value. 
	 */
	@ManyToMany(mappedBy="customers",fetch=FetchType.EAGER)
	private Set<Coupon>coupons;

 
	@OneToOne(mappedBy="customer")
	@JsonIgnore
	private ShoppingCart cart;

	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public Set<Coupon> getCoupons() {
		return coupons;
	}
	public Customer() {
		super();
	}
	public Customer(String firstName, String lastName, String email, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	public boolean isPrime() {
		return isPrime;
	}
	public void setPrime(boolean isPrime) {
		this.isPrime = isPrime;
	}
	public ShoppingCart getCart() {
		return cart;
	}
	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", coupons=" + coupons + ", cart=" + cart + "]";
	}


}


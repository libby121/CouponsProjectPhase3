package com.example.demo.beans;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "coupons")
 
public class Coupon implements Serializable {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	 
	private Category category;
	 
	private String title;
	 
	private String description;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	 
	private int amount;
	 @Column(scale=2) 
	private double price;
	
	private String image;
	@ManyToOne
	private Company company;
	@Column(name = "discount_status", nullable = true) // which name do i use in update
	private boolean isSalePrice;
	@JsonIgnore//other wise i get infinite loop in postman

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "customers_vs_coupons", joinColumns = @JoinColumn(name = "coupon_id"), 
	inverseJoinColumns = @JoinColumn(name = "customer_id"))
	Set<Customer>customers ;

//	@ManyToMany(mappedBy="cartItems")
//	Set<ShoppingCart>carts;
	public Coupon() {
		super();
	}

	

	public Coupon(int id, Category category,String title, String description, Date startDate, Date endDate, int amount,
			double price) {
		super();
		this.id = id;
 		this.title = title;
 		this.category=category;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		 System.out.println("ctor");

	}

	 
	public Coupon(Category category, String title, String description, Date startDate, Date endDate, int amount,
			double price ) {
		super();
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		 
		this.isSalePrice = false;

	}
 

	 
	 
//for test
	public Coupon( int id, String title, int amount, double price) {
		super();
		this.id=id;
		this.title = title;
		this.amount = amount;
		this.price = price;
		 
	}



	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	
	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isSalePrice() {
		return isSalePrice;
	}

	public void setSalePrice(boolean isSalePrice) {
		this.isSalePrice = isSalePrice;
	}

	public int getId() {
		return id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Coupon other = (Coupon) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/**
	 * hashCode- numeric representation of the object in memory.
	 */
	 

	public Set<Customer> getCustomers() {
		return customers;
	}
	/**
	 * An overloading of getPrice() method.
	 * If a customer is a prime customer the coupon's price is always lower.
	 * Otherwise the regular price is set to it.
	 * 
	 * @param c
	 * @return
	 */
	public double getPrice()
	{
		return price;
	}
	public double getPrice(Customer c) {
		if(c.isPrime())
		return price*0.95;
		return price;
	}



	@Override
	public String toString() {
		return "Coupon [id=" + id + "]";
	}
	
	 
}

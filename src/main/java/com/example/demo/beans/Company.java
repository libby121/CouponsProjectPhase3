package com.example.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *  Entity tag-Hibernate annotation. Indicates an entity to be mapped to a table in the dataBase.
 *  Table tag-enables changing some meta-data of the created table, such as name.
 *  id tag-an annotation that represents a primary key by which the entity will be identified.
 *  GeneratedValue(Identity) tag-the marked property will be automatically incremented by one in every creation
 *  of the class object. 
 *  Column tag-java property to be mapped to a column.
 *  OneToMany tag-In this case, each company entity 'owns' a list of coupons. The coupons property is not mapped to a
 *  column, but it rather represents a relation.
 *  mappedBy-marks that the property in this bean does not require a separate table since it's the opposite side of @ManyToOne 
 *  relation which already exist in another bean.
 *  fetch-There are different strategies to get or fetch the data of related entities. "eager" means that 
 *  the fetched data includes information that might not be otherwise critical or essential to fetch.But is needed in this case.
 *
 */
@Entity
@Table(name="companies")
public class Company {
	
	@JsonIgnore//otherwise i get infinite loop in postman and parse error
	@OneToMany(mappedBy="company", fetch=FetchType.EAGER)
	 List<Coupon> coupons;
	private String name;
	private String email;
	private String password;
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;
	private LocalDateTime lastUpdate;
	 @Column(scale=2)
	private Double balance;
	
	/**
	 * Hibernate requires an empty Constructor for fetching the data (encapsulated as an object) from database.
	 */
	public Company() {
		 
	}
	/**
	 * constructor for adding a company. id is automatically generated, balance equals 0.
 	 * @param email-company email
	 * @param password-company password
	 */
	public Company( String email, String password) {
		 
		this.name = "name";
		this.email = email;
		this.password = password;
		this.balance= (double) 0;
	}
	
	/**
	 * An optional constructor for updating the company using its id
	 * @param id-company id
	 * @param name-company name
	 * @param email-company email
	 * @param password-company password
	 * @param balance-company balance
	 */
	public Company(UUID id, String name, String email, String password, Double balance) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.balance = balance;
	}
	public String getName() {
		return name+"121";
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getBalance() {
		return balance;
	}

	 

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public UUID getId() {
		return id;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}
	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	
	
}

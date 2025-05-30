package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *  Entity tag-Hibernate annotation. Indicates an entity to be mapped to a table in the dataBase.
 *  Table tag-enables changing some meta-data of the created table, such as userName.
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
 public class Customer  extends MyUser {

	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="is_Prime")
	private boolean isPrime;
   	 @Column(scale=2)
	private double revenue;
	/**
	 * Set (instead of a List) makes sure that both coupon_id and customer_id are the primary key together,
	 * a Set in Java only accepts a unique value. 
	 */

	@ManyToMany(mappedBy="customers",fetch=FetchType.EAGER)
	 Set<Coupon>coupons;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "customer_role")
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private Set<MyRole> myRoles;
	public Collection<MyRole> getRoles() {
		return myRoles;
	}
    public Customer(String username, String password, String email) {
		super(username,password,email);
		this.coupons =new HashSet<>();

    }
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
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
		public Set<Coupon> getCoupons() {
		return coupons;
	}
	public Customer() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + super.getId().variant();
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
        return super.id == other.id;
    }
	public boolean isPrime() {
		return isPrime;
	}
	public void setPrime(boolean isPrime) {
		this.isPrime = isPrime;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", coupons=" + coupons +"]";
	}


}

package com.example.demo.model;

import com.example.demo.entity.Coupon;
import com.example.demo.entity.MyRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
@Getter
@Setter
@RequiredArgsConstructor
public class CustomerDTO implements Serializable {
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isPrime;
    private double revenue;

    Set<Coupon> coupons;

    private Set<MyRole> myRoles;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isPrime() {
        return isPrime;
    }

    public double getRevenue() {
        return revenue;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public Set<MyRole> getRoles() {
        return myRoles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPrime(boolean prime) {
        isPrime = prime;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void setRoles(Set<MyRole> myRoles) {
        this.myRoles = myRoles;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }





}

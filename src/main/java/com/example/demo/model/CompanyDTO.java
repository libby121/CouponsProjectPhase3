package com.example.demo.model;

import com.example.demo.entity.Coupon;
import com.example.demo.entity.MyRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer object-used for transferring objects across domains
 * (separated from DAO-data access object used for crud operations)
 */
public class CompanyDTO implements Serializable {

    private LocalDateTime lastUpdate;
    private double balance;
    private Set<Coupon> coupons;
    private Set<MyRole> myRoles;
    private String userName;
    private String email;
    private String password;

    public CompanyDTO() {
    }

    public CompanyDTO(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void setRoles(Set<MyRole> myRoles) {
        this.myRoles = myRoles;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public Set<MyRole> getRoles() {
        return myRoles;
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


}

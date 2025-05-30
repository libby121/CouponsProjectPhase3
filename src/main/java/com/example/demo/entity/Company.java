package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="companies")
public class Company extends MyUser{
    @Column(name="last_update")
    private LocalDateTime lastUpdate;
    @Column
    private double balance;
    @JsonIgnore//otherwise i get infinite loop in postman and parse error
    @OneToMany(mappedBy="company", fetch= FetchType.EAGER)
    Set<Coupon> coupons;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "company_role")
    @JsonIgnore
    private Set<MyRole> myRoles;
    public Set<MyRole> getRoles() {
        return myRoles;
    }

    public Company() {
        super();
        this.balance=0;
        this.lastUpdate=LocalDateTime.now();
        this.coupons=new HashSet<>();
        this.myRoles =new HashSet<MyRole>();
        myRoles.add(new MyRole("MANAGER"));
        this.lastUpdate=LocalDateTime.now();
    }

    public Company(String userName, String password, String email) {
        super(userName,password,email);
        this.balance=0;
        this.lastUpdate=LocalDateTime.now();
        this.coupons=new HashSet<>();
        this.myRoles =new HashSet<MyRole>();
        myRoles.add(new MyRole("MANAGER"));
    }
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public Set<Coupon> getCoupons() {
        return coupons;
    }


    public void setRoles(Set<MyRole> myRoles) {

    }
}

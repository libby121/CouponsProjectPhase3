package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@MappedSuperclass
 public abstract class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;
    @Column(name="userName")
    protected String userName;
    protected String email;
    protected String password;

//add the roles?
    public MyUser(String userName, String password, String email) {

        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public MyUser() {

    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
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

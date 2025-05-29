package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.UUID;
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    protected UUID id;
    protected String name;
    protected String email;
    protected String password;

    public User() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

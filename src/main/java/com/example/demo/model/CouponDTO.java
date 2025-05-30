package com.example.demo.model;

import com.example.demo.entity.Category;
import com.example.demo.entity.Company;
import com.example.demo.entity.Coupon;
import com.example.demo.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class CouponDTO implements Serializable {

    private int id;
    private Category category;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int amount;
    private double price;
    private String image;
    private Company company;
    private boolean isSalePrice;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setSalePrice(boolean salePrice) {
        isSalePrice = salePrice;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public Company getCompany() {
        return company;
    }

    public boolean isSalePrice() {
        return isSalePrice;
    }
}

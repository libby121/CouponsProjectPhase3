package com.example.demo.entity;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

 public enum Permission {

    ADMIN_READ("admin_read"),
    ADMIN_UPDATE("admin_update"),
    ADMIN_DELETE("admin_delete"),

    MANAGER_READ("manager_read"),
    MANAGER_UPDATE("manager_update"),
    MANAGER_DELETE("manager_delete"),

    SUBSCRIBER_READ("subscriber_read"),
    SUBSCRIBER_UPDATE("subscriber_read"),
    SUBSCRIBER_DELETE("subscriber_read");

//    BUYER_READ("buyer_delete"),
//    BUYER_UPDATE("buyer_delete"),
//    BUYER_DELETE("buyer_delete")
    ;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    private final String permission;
}

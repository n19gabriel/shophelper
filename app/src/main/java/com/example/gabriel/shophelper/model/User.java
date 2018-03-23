package com.example.gabriel.shophelper.model;

/**
 * Created by gabriel on 23.03.18.
 */

public class User {
    private String id;
    private String roles;

    public User() {
    }

    public User(String id, String roles) {
        this.id = id;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}

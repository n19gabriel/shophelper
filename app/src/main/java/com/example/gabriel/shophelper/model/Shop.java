package com.example.gabriel.shophelper.model;

/**
 * Created by gabriel on 22.03.18.
 */

public class Shop {
    private String id;
    private String name;

    public Shop() {
    }

    public Shop(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

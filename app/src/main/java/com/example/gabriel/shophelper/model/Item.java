package com.example.gabriel.shophelper.model;

/**
 * Created by gabriel on 23.03.18.
 */

public class Item {
    private String id;
    private String barcode;
    private String name;
    private String price;
    private String quantity;
    private String id_Shop;
    private String id_Image;

    public Item() {
    }

    public Item(String id, String barcode, String name, String price, String id_Shop) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.quantity = "1";
        this.id_Shop = id_Shop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId_Shop() {
        return id_Shop;
    }

    public void setId_Shop(String id_Shop) {
        this.id_Shop = id_Shop;
    }

    public String getId_Image() {
        return id_Image;
    }

    public void setId_Image(String id_Image) {
        this.id_Image = id_Image;
    }
}

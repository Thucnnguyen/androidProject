package com.example.instagram.model;

public class Location {
    private int id;
    private String address;
    private int productId;

    public Location(int id, String address, int productId) {
        this.id = id;
        this.address = address;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}

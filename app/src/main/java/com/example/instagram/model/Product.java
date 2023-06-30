package com.example.instagram.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private int categoryId;
    private float price;
    private String description;

    private String image;
    private int id;

    public Product() {
    }

    public Product(String name, int categoryId, float price, String description, String image, int id) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.description = description;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDesciption() {
        return description;
    }

    public void setDesciption(String desciption) {
        this.description = desciption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", id=" + id +
                '}';
    }
}

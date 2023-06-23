package com.example.instagram;

public class Product {
    private String name;
    private int categoryId;
    private float price;
    private String desciption;
    private String image;
    private int id;
//    private int cate_id;



    public Product() {
    }

    public Product(String name, int categoryId, float price, String desciption, String image, int id) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.desciption = desciption;
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
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
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
}

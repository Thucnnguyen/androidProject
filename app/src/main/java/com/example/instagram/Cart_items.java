package com.example.instagram;

public class Cart_items {
    private String customerId;
    private int ProductID;
    private int Quantity;
    private int id;

    public Cart_items(int productID, int quantity, int id, String cartID) {
    }

    public Cart_items(String customerId, int productID, int quantity, int id) {
        customerId = customerId;
        ProductID = productID;
        Quantity = quantity;
        this.id = id;
    }

    public Cart_items(int productID, int quantity, String cartID) {
    }

    public Cart_items(String customerId, int productID, int quantity) {
        customerId = customerId;
        ProductID = productID;
        Quantity = quantity;
    }

    public String getCartID() {
        return customerId;
    }

    public void setCartID(String cartID) {
        customerId = customerId;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
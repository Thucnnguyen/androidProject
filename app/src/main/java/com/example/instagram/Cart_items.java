package com.example.instagram;

public class Cart_items {
    private int customerId;
    private int ProductID;
    private int Quantity;
    private int id;

    public Cart_items() {
    }

    public Cart_items(int CustomerId, int productID, int quantity, int id) {
        customerId = CustomerId;
        ProductID = productID;
        Quantity = quantity;
        this.id = id;
    }
    public Cart_items(int CustomerId, int productID, int quantity) {
        customerId = CustomerId;
        ProductID = productID;
        Quantity = quantity;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int cartID) {
        customerId = cartID;
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
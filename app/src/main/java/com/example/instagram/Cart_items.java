package com.example.instagram;

public class Cart_items {
    private String CustomerID;
    private int ProductID;
    private int Quantity;
    private int id;

    public Cart_items() {
    }

    public Cart_items(String customerID, int productID, int quantity, int id) {
        CustomerID = customerID;
        ProductID = productID;
        Quantity = quantity;
        this.id = id;
    }

    public String getCartID() {
        return CustomerID;
    }

    public void setCartID(String customerID) {
        CustomerID = customerID;
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
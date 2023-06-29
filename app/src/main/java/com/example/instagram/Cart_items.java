package com.example.instagram;

public class Cart_items {
    private String CartID;
    private int ProductID;
    private int Quantity;
    private int id;

    public Cart_items() {
    }

    public Cart_items(String cartID, int productID, int quantity, int id) {
        CartID = cartID;
        ProductID = productID;
        Quantity = quantity;
        this.id = id;
    }

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
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
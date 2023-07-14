package com.example.instagram.model;

public class OrderItem {
    private int id;
    private int order_id;
    private int product_id;
    private double quantity;
    private double price;
    private double discount;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order_id=" + order_id +
                ", product_id=" + product_id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return order_id;
    }

    public void setOrderId(int orderId) {
        this.order_id = orderId;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int productId) {
        this.product_id = productId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public OrderItem(int id, int orderId, int productId, double quantity, double price, double discount) {
        this.id = id;
        this.order_id = orderId;
        this.product_id = productId;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }
}

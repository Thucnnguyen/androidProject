package com.example.instagram.model;

import java.util.Date;

public class Order {

    private int id;
    private int customer_id;
    private String order_status;
    private Long order_date;
//    private Long ship_date;
//    private int staff_id;
//    private int store_id;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customer_id +
                ", oderStatus='" + order_status + '\'' +
                ", orderDate=" + order_date +


                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(int customerId) {
        this.customer_id = customerId;
    }

    public String getOderStatus() {
        return order_status;
    }

    public void setOderStatus(String oderStatus) {
        this.order_status = oderStatus;
    }

    public Long getOrderDate() {
        return order_date;
    }

    public void setOrderDate(Long orderDate) {
        this.order_date = orderDate;
    }

//    public Long getShipDate() {
//        return ship_date;
//    }
//
//    public void setShipDate(Long shipDate) {
//        this.ship_date = shipDate;
//    }
//
//    public int getStaffId() {
//        return staff_id;
//    }
//
//    public void setStaffId(int staffId) {
//        this.staff_id = staffId;
//    }
//
//    public int getStoreId() {
//        return store_id;
//    }
//
//    public void setStoreId(int storeId) {
//        this.store_id = storeId;
//    }

    public Order() {
    }

    public Order(int id, int customerId, String oderStatus, Long orderDate, Long shipDate, int staffId, int storeId) {

        this.id = id;
        this.customer_id = customerId;
        this.order_status = oderStatus;
        this.order_date = orderDate;
//        this.ship_date = shipDate;
//        this.staff_id = staffId;
//        this.store_id = storeId;
    }
    public Order( int customerId, String oderStatus, Long orderDate) {

        this.customer_id = customerId;
        this.order_status = oderStatus;
        this.order_date = orderDate;
//        this.ship_date = shipDate;
//        this.staff_id = staffId;
//        this.store_id = storeId;
    }
}

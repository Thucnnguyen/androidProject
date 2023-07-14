package com.example.instagram.model;

import android.icu.util.LocaleData;
import android.os.LocaleList;

import java.time.LocalDate;
import java.util.Date;

public class Order {
    private int id;
    private int customer_id;
    private String order_status;
    private String order_date;

    public Order() {
    }

    public Order(int customer_id, String order_status, String order_date) {
        this.customer_id = customer_id;
        this.order_status = order_status;
        this.order_date = order_date;
    }

    public Order(int id, int customer_id, String order_status, String order_date) {
        this.id = id;
        this.customer_id = customer_id;
        this.order_status = order_status;
        this.order_date = order_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}

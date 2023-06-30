package com.example.instagram.model;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Date;

public class Cart  {
    private String CustomerID;
    private Date Date;
    private int id;

    public Cart() {
    }

    public Cart(String customerID, java.util.Date date, int id) {
        CustomerID = customerID;
        Date = date;
        this.id = id;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
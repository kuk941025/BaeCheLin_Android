package com.leaderpower.baechelin_owner_android.model;

import java.util.Date;

public class Order {
    private String address, food_ordered, request, user_phone;
    private int payment_method, status, total_price;
    private Date created_at, delivered_at;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFood_ordered() {
        return food_ordered;
    }

    public void setFood_ordered(String food_ordered) {
        this.food_ordered = food_ordered;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(Date delivered_at) {
        this.delivered_at = delivered_at;
    }
}

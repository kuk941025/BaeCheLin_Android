package com.leaderpower.baechelin_owner_android.model;

import java.util.Date;

public class Order {
    private String ordered_food, address;
    private int price, status;
    private Date time;

    public Order() {
    }
    public Order(String address, String ordered_food){
        this.address = address;
        this.ordered_food = ordered_food;
    }
    public String getOrdered_food() {
        return ordered_food;
    }

    public void setOrdered_food(String ordered_food) {
        this.ordered_food = ordered_food;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

package com.leaderpower.baechelin_owner_android.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    private String address_detail, address_jibun, address_road, coupon_type, request, status, user_phone, id, delivered_at, food_ordered;
    private Date created_at;
    private int coupon_amount, payment_method, point_amount, total_price, mode, delivery_time;
    private boolean timer_on;

    private ArrayList<Food> food;

    public Order() {
        mode = 0;
        timer_on = false;
    }

    public boolean isTimer_on() {
        return timer_on;
    }

    public void setTimer_on(boolean timer_on) {
        this.timer_on = timer_on;
    }

    public void setFoodOrdered() {
        String ordered = "";
        Food first_food = food.get(0);
        if (food.size() == 1) ordered = first_food.getName() + " " + first_food.getCount() + "개";
        else ordered = first_food.getName() + " 외" + (food.size() - 1) + "개";

        this.food_ordered = ordered;
    }

    public String getFood_ordered() {
        return food_ordered;
    }

    public int getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(String delivered_at) {
        this.delivered_at = delivered_at;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getAddress_jibun() {
        return address_jibun;
    }

    public void setAddress_jibun(String address_jibun) {
        this.address_jibun = address_jibun;
    }

    public String getAddress_road() {
        return address_road;
    }

    public void setAddress_road(String address_road) {
        this.address_road = address_road;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            this.created_at = sdf.parse(created_at);
        } catch (Exception e) {
            this.created_at = new Date();
        }

    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(int coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public int getPoint_amount() {
        return point_amount;
    }

    public void setPoint_amount(int point_amount) {
        this.point_amount = point_amount;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public ArrayList<Food> getFood() {
        return food;
    }

    public void setFood(ArrayList<Food> food) {
        this.food = food;
    }
}
//public class Order {
//    private String address, food_ordered, request, user_phone, id;
//    private int payment_method, status, total_price, mode = 0;
//    private Date created_at, delivered_at;
//    private ArrayList<Foods> foods;
//
//    //mode == 0 default
//    //mode == 1 confirm clicked
//    //mode == 2 reject clicked
//    public String getId() {
//        return id;
//    }
//
//    public ArrayList<Foods> getFoods() {
//        return foods;
//    }
//
//    public void setFoods(ArrayList<Foods> foods) {
//        this.foods = foods;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public int getMode() {
//        return mode;
//    }
//
//    public void setMode(int mode) {
//        this.mode = mode;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getFood_ordered() {
//        return food_ordered;
//    }
//
//    public void setFood_ordered(String food_ordered) {
//        this.food_ordered = food_ordered;
//    }
//
//    public String getRequest() {
//        return request;
//    }
//
//    public void setRequest(String request) {
//        this.request = request;
//    }
//
//    public String getUser_phone() {
//        return user_phone;
//    }
//
//    public void setUser_phone(String user_phone) {
//        this.user_phone = user_phone;
//    }
//
//    public int getPayment_method() {
//        return payment_method;
//    }
//
//    public void setPayment_method(int payment_method) {
//        this.payment_method = payment_method;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public int getTotal_price() {
//        return total_price;
//    }
//
//    public void setTotal_price(int total_price) {
//        this.total_price = total_price;
//    }
//
//    public Date getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(Date created_at) {
//        this.created_at = created_at;
//    }
//
//    public Date getDelivered_at() {
//        return delivered_at;
//    }
//
//    public void setDelivered_at(Date delivered_at) {
//        this.delivered_at = delivered_at;
//    }
//}

package com.leaderpower.baechelin_owner_android.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OwnerItem implements Serializable {
    private String address, address_detail, bcode, corp_image, corp_name, corp_num, corp_serial_num,store_type,
            corp_shop_name, delivery_option, denied_reason, oid, report_image, shop_description, shop_image, shop_name, zone_code, uid;
    private int business_status, is_working;
    private Date store_coupon, created_at;
    private List<Notification> token;

    public List<Notification> getToken() {
        return token;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setToken(List<Notification> token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getCorp_image() {
        return corp_image;
    }

    public void setCorp_image(String corp_image) {
        this.corp_image = corp_image;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getCorp_num() {
        return corp_num;
    }

    public void setCorp_num(String corp_num) {
        this.corp_num = corp_num;
    }

    public String getCorp_serial_num() {
        return corp_serial_num;
    }

    public void setCorp_serial_num(String corp_serial_num) {
        this.corp_serial_num = corp_serial_num;
    }

    public String getCorp_shop_name() {
        return corp_shop_name;
    }

    public void setCorp_shop_name(String corp_shop_name) {
        this.corp_shop_name = corp_shop_name;
    }

    public String getDelivery_option() {
        return delivery_option;
    }

    public void setDelivery_option(String delivery_option) {
        this.delivery_option = delivery_option;
    }

    public String getDenied_reason() {
        return denied_reason;
    }

    public void setDenied_reason(String denied_reason) {
        this.denied_reason = denied_reason;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getReport_image() {
        return report_image;
    }

    public void setReport_image(String report_image) {
        this.report_image = report_image;
    }

    public String getShop_description() {
        return shop_description;
    }

    public void setShop_description(String shop_description) {
        this.shop_description = shop_description;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getZone_code() {
        return zone_code;
    }

    public void setZone_code(String zone_code) {
        this.zone_code = zone_code;
    }

    public int getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(int business_status) {
        this.business_status = business_status;
    }

    public int getIs_working() {
        return is_working;
    }

    public void setIs_working(int is_working) {
        this.is_working = is_working;
    }

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public Date getStore_coupon() {
        return store_coupon;
    }

    public void setStore_coupon(Date store_coupon) {
        this.store_coupon = store_coupon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

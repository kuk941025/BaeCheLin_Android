package com.leaderpower.baechelin_owner_android.model;

import java.util.ArrayList;
import java.util.Date;

public class BusinessInfo {
    private String report_image, corp_num, name, corp_serial_num, denied_reason, corp_name, email, corp_image;
    private int business_status;
    private Date created_at, customer_coupon;
    private ArrayList<BusinessOwners> owners;

    public ArrayList<BusinessOwners> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<BusinessOwners> owners) {
        this.owners = owners;
    }

    public String getReport_image() {
        return report_image;
    }

    public void setReport_image(String report_image) {
        this.report_image = report_image;
    }

    public String getCorp_num() {
        return corp_num;
    }

    public void setCorp_num(String corp_num) {
        this.corp_num = corp_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorp_serial_num() {
        return corp_serial_num;
    }

    public void setCorp_serial_num(String corp_serial_num) {
        this.corp_serial_num = corp_serial_num;
    }

    public String getDenied_reason() {
        return denied_reason;
    }

    public void setDenied_reason(String denied_reason) {
        this.denied_reason = denied_reason;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCorp_image() {
        return corp_image;
    }

    public void setCorp_image(String corp_image) {
        this.corp_image = corp_image;
    }

    public int getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(int business_status) {
        this.business_status = business_status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getCustomer_coupon() {
        return customer_coupon;
    }

    public void setCustomer_coupon(Date customer_coupon) {
        this.customer_coupon = customer_coupon;
    }
}

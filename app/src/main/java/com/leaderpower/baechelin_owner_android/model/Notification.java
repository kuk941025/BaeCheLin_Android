package com.leaderpower.baechelin_owner_android.model;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
    private String corp_name, shop_name, uid, token_id, token;
    private Date created_at;

    public String getToken_id() {
        return token_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}

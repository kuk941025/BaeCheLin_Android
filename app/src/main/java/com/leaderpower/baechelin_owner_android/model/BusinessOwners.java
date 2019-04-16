package com.leaderpower.baechelin_owner_android.model;

import java.util.List;

public class BusinessOwners {
    private String oid, name;
    private List<String> token;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getToken() {
        return token;
    }

    public void setToken(List<String> token) {
        this.token = token;
    }
}

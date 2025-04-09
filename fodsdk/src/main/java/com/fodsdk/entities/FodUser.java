package com.fodsdk.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FodUser implements Serializable {

    @SerializedName("uid")
    private String uid;

    @SerializedName("token")
    private String token;

    @SerializedName("phone")
    private String phone;

    @SerializedName("account")
    private String account;

    @SerializedName("real_name")
    private boolean realName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isRealName() {
        return realName;
    }

    public void setRealName(boolean realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "FodUser{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", phone='" + phone + '\'' +
                ", account='" + account + '\'' +
                ", realName=" + realName +
                '}';
    }
}

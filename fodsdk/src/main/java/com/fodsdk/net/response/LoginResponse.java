package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("uid")
    private String uid;
    @SerializedName("account")
    private String account;
    @SerializedName("phone")
    private String phone;
    @SerializedName("is_new_register")
    int isNewRegister;
    @SerializedName("type")
    private int type;
    @SerializedName("is_report_medium")
    private int isReportMedium;
    @SerializedName("real_info")
    private LoginRealInfo realInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsNewRegister() {
        return isNewRegister;
    }

    public void setIsNewRegister(int isNewRegister) {
        this.isNewRegister = isNewRegister;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsReportMedium() {
        return isReportMedium;
    }

    public void setIsReportMedium(int isReportMedium) {
        this.isReportMedium = isReportMedium;
    }

    public LoginRealInfo getRealInfo() {
        return realInfo;
    }

    public void setRealInfo(LoginRealInfo realInfo) {
        this.realInfo = realInfo;
    }

    @Override
    public String toString() {
        return "AccountRegisterResponse{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", account='" + account + '\'' +
                ", phone='" + phone + '\'' +
                ", isNewRegister=" + isNewRegister +
                ", type=" + type +
                ", isReportMedium=" + isReportMedium +
                ", realInfo=" + realInfo +
                '}';
    }
}

package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class PayInfoResponse {

    @SerializedName("pay_token")
    String payToken;

    public String getPayToken() {
        return payToken;
    }

    public void setPayToken(String payToken) {
        this.payToken = payToken;
    }

    @Override
    public String toString() {
        return "PayInfoResponse{" +
                "payToken='" + payToken + '\'' +
                '}';
    }
}

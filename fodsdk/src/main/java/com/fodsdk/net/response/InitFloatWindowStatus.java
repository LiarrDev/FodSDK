package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class InitFloatWindowStatus {
    @SerializedName("status")
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InitFloatWindowStatus{" +
                "status=" + status +
                '}';
    }
}

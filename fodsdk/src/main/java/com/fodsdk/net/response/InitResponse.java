package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class InitResponse {
    @SerializedName("float_window_status")
    private InitFloatWindowStatus floatWindowStatus;

    public InitFloatWindowStatus getFloatWindowStatus() {
        return floatWindowStatus;
    }

    public void setFloatWindowStatus(InitFloatWindowStatus floatWindowStatus) {
        this.floatWindowStatus = floatWindowStatus;
    }

    @Override
    public String toString() {
        return "InitResponse{" +
                "floatWindowStatus=" + floatWindowStatus +
                '}';
    }
}


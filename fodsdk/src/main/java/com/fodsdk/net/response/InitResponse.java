package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class InitResponse {
    @SerializedName("float_window_status")
    private InitFloatWindowStatus floatWindowStatus;
    @SerializedName("show_phone_status")
    private InitShowPhoneStatus showPhoneStatus;

    public InitFloatWindowStatus getFloatWindowStatus() {
        return floatWindowStatus;
    }

    public void setFloatWindowStatus(InitFloatWindowStatus floatWindowStatus) {
        this.floatWindowStatus = floatWindowStatus;
    }

    public InitShowPhoneStatus getShowPhoneStatus() {
        return showPhoneStatus;
    }

    public void setShowPhoneStatus(InitShowPhoneStatus showPhoneStatus) {
        this.showPhoneStatus = showPhoneStatus;
    }

    @Override
    public String toString() {
        return "InitResponse{" +
                "floatWindowStatus=" + floatWindowStatus +
                ", showPhoneStatus=" + showPhoneStatus +
                '}';
    }
}


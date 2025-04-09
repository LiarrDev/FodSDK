package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class AccountRegisterRealInfo {
    @SerializedName("status")
    private int status;
    @SerializedName("is_force_real_name")
    private int isForceRealName;
    @SerializedName("url")
    private String url;
    @SerializedName("time")
    private int time;
    @SerializedName("is_real_name")
    private int isRealName;
    @SerializedName("msg")
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsForceRealName() {
        return isForceRealName;
    }

    public void setIsForceRealName(int isForceRealName) {
        this.isForceRealName = isForceRealName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(int isRealName) {
        this.isRealName = isRealName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AccountRegisterRealInfo{" +
                "status=" + status +
                ", isForceRealName=" + isForceRealName +
                ", url='" + url + '\'' +
                ", time=" + time +
                ", isRealName=" + isRealName +
                ", msg='" + msg + '\'' +
                '}';
    }
}

package com.fodsdk.net.response;

import com.google.gson.annotations.SerializedName;

public class DoPayResponse {

    @SerializedName("order")
    String order;
    @SerializedName("url")
    String url;
    @SerializedName("scheme")
    String scheme;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return "DoPayResponse{" +
                "order='" + order + '\'' +
                ", url='" + url + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }
}

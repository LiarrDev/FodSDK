package com.fodsdk.net;

public class FodBaseApi {

    private final static String baseUrl = "http://user.gzfenghou.cn/";
    private final String name;

    public FodBaseApi(String name) {
        this.name = name;
    }

    public String getUrl() {
        return baseUrl + name;
    }
}

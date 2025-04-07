package com.fodsdk.net;

public abstract class FodBaseApi {

    private final String name;

    public FodBaseApi(String name) {
        this.name = name;
    }

    public String getUrl() {
        return getHost() + name;
    }

    public abstract String getHost();
}

package com.fodsdk.core;

public class FodSDK extends FodSDKCore {

    private FodSDK() {
    }

    private static final FodSDK instance = new FodSDK();

    public static FodSDK get() {
        return instance;
    }
}

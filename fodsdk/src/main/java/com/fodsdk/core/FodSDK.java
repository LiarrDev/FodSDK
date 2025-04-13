package com.fodsdk.core;

public class FodSDK extends FodSDKCore {

    private static final FodSDK instance = new FodSDK();

    private FodSDK() {
    }

    public static FodSDK get() {
        return instance;
    }
}

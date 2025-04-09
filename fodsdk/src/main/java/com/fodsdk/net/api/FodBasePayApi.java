package com.fodsdk.net.api;

import com.fodsdk.core.FodConstants;

public class FodBasePayApi extends FodBaseApi {

    public FodBasePayApi(String name) {
        super(name);
    }

    @Override
    public String getHost() {
        return FodConstants.FOD_API_HOST_PAY;
    }
}

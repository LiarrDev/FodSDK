package com.fodsdk.net.api;

import com.fodsdk.core.FodConstants;

public class FodBasePayApi extends FodBaseApi {

    public FodBasePayApi(String name) {
        super(name);
    }

    @Override
    public String getHost() {
        return FodConstants.Inner.API_HOST_PAY;
    }
}

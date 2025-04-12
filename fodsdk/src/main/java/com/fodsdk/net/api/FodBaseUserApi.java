package com.fodsdk.net.api;

import com.fodsdk.core.FodConstants;

public class FodBaseUserApi extends FodBaseApi {

    public FodBaseUserApi(String name) {
        super(name);
    }

    @Override
    public String getHost() {
        return FodConstants.Inner.API_HOST_USER;
    }
}

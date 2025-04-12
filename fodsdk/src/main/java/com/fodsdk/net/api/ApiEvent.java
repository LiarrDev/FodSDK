package com.fodsdk.net.api;

import com.fodsdk.core.FodConstants;

public class ApiEvent extends FodBaseApi {

    public ApiEvent() {
        super("");
    }

    @Override
    public String getHost() {
        return FodConstants.Inner.API_HOST_EVENT;
    }
}

package com.fodsdk.net.api;

import android.net.Uri;

import com.fodsdk.entities.FodGameConfig;

public class ApiPrivacyPolicy extends FodBaseUserApi {

    private final FodGameConfig config;

    public ApiPrivacyPolicy(FodGameConfig config) {
        super("ApiGetReivarcyUrl.php");
        this.config = config;
    }

    @Override
    public String getUrl() {
        Uri.Builder builder = Uri.parse(getHost() + getApiName()).buildUpon();
        if (config != null) {
            builder.appendQueryParameter("pid", String.valueOf(config.getPId()))
                    .appendQueryParameter("gid", String.valueOf(config.getGId()))
                    .appendQueryParameter("osid", String.valueOf(config.getOsId()))
                    .appendQueryParameter("client", String.valueOf(config.getClient()))
                    .appendQueryParameter("pkid", String.valueOf(config.getPkId()));
        }
        return builder.toString();
    }
}

package com.fodsdk.settings;

import com.fodsdk.utils.SPUtil;

public class GlobalSettings {

    private static final String IS_FIRST_LAUNCH = "is_first_launch";
    private static final String LAST_LOGIN_TOKEN = "last_login_token";

    public static boolean isFirstLaunch() {
        return SPUtil.get(IS_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunch(boolean isFirstLaunch) {
        SPUtil.put(IS_FIRST_LAUNCH, isFirstLaunch);
    }

    public static String getLastLoginToken() {
        return SPUtil.get(LAST_LOGIN_TOKEN, "");
    }

    public static void setLastLoginToken(String token) {
        SPUtil.put(LAST_LOGIN_TOKEN, token);
    }
}

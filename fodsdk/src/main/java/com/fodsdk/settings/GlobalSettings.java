package com.fodsdk.settings;

import com.fodsdk.utils.SPUtil;

public class GlobalSettings {

    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";

    public static boolean isFirstLaunch() {
        return SPUtil.get(IS_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunch(boolean isFirstLaunch) {
        SPUtil.put(IS_FIRST_LAUNCH, isFirstLaunch);
    }
}

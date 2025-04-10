package com.fodsdk.settings;

import com.fodsdk.core.FodSDK;
import com.fodsdk.utils.SPUtil;

public class UserSettings {

    private static final String TODAY_ALLOW_PLAY_TIME = "today_allow_play_time";

    public static int getTodayAllowPlayTime() {
        return SPUtil.get(TODAY_ALLOW_PLAY_TIME + "_" + uid(), Integer.MAX_VALUE);
    }

    public static void setTodayAllowPlayTime(int time) {
        SPUtil.put(TODAY_ALLOW_PLAY_TIME + "_" + uid(), time);
    }

    private static String uid() {
        return FodSDK.get().getUser().getUid();
    }
}

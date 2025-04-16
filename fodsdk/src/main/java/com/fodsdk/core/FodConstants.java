package com.fodsdk.core;

public class FodConstants {

    public static class Inner {
        public static String GAME_CONFIG_FILE = "fod_game_config.json";
        public static String API_HOST_USER = "http://user.gzfenghou.cn/";
        public static String API_HOST_PAY = "http://pay.gzfenghou.cn/";
        public static String API_HOST_EVENT = "http://log.gzfenghou.cn";
        public static String URL_PAYMENT = "http://static-manage.gzfenghou.cn/payMent/";
        public static String URL_USER_CENTER = "http://static-manage.gzfenghou.cn/userCenter/index.html";
    }

    public static class Code {
        public static final int SUCCESS = 1;
        public static final int FAILURE = -1;
    }

    public static class Event {
        public static final String SCENE_OPEN = "open";
        public static final String SCENE_LOGIN = "login";
        public static final String SCENE_BEFORE_ENTRY = "entry_before";
        public static final String SCENE_ENTRY = "entry";
        public static final String SCENE_CREATE_ROLE = "role";
        public static final String SCENE_LEVEL = "level";
        public static final String SCENE_ONLINE = "online";
        public static final String SCENE_ERROR = "sdk_error";
    }

    public static class SCHEME {
        public static final String WECHAT = "weixin://";
        public static final String ALIPAY = "alipays://";
    }
}

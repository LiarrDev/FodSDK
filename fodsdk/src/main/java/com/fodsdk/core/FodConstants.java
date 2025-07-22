package com.fodsdk.core;

public class FodConstants {

    public static class Inner {
        public static String GAME_CONFIG_FILE = "fod_game_config.json";
        public static String API_HOST_USER = "https://user.gzxiaowanzi.cn/";
        public static String API_HOST_PAY = "https://pay.gzxiaowanzi.cn/";
        public static String API_HOST_EVENT = "https://log.gzxiaowanzi.cn";
        public static String URL_PAYMENT = "https://static-manage.gzxiaowanzi.cn/payMent/";
        public static String URL_USER_CENTER = "https://static-manage.gzxiaowanzi.cn/userCenter/index.html";
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

    public static class PAY {
        public static final String GOODS_ID = "goods_id";
        public static final String GOODS_NAME = "goods_name";
        public static final String GOODS_DESC = "goods_desc";
        public static final String GOODS_COUNT = "goods_count";
        public static final String GOODS_PRICE = "goods_price";
        public static final String PAY_TYPE = "pay_type";
    }
}

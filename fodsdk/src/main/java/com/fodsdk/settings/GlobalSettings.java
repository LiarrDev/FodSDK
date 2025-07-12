package com.fodsdk.settings;

import com.fodsdk.utils.SPUtil;

import java.util.HashSet;
import java.util.Set;

public class GlobalSettings {

    private static final String IS_FIRST_LAUNCH = "is_first_launch";
    private static final String LAST_LOGIN_TOKEN = "last_login_token";
    private static final String LOGIN_BEFORE = "login_before";
    private static final String ERROR_SET = "error_set";

    public static boolean isFirstLaunch() {
        return SPUtil.get(IS_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunch(boolean isFirstLaunch) {
        SPUtil.put(IS_FIRST_LAUNCH, isFirstLaunch);
    }

    public static boolean hasLoginBefore() {
        return SPUtil.get(LOGIN_BEFORE, false);
    }

    public static void setLoginBefore(boolean loginBefore) {
        SPUtil.put(LOGIN_BEFORE, loginBefore);
    }

    public static String getLastLoginToken() {
        return SPUtil.get(LAST_LOGIN_TOKEN, "");
    }

    public static void setLastLoginToken(String token) {
        SPUtil.put(LAST_LOGIN_TOKEN, token);
    }

    public static void addError(String error) {
        Set<String> set = getError();
        set.add(error);
        SPUtil.put(ERROR_SET, set);
    }

    public static void removeError(String error) {
        Set<String> set = getError();
        set.remove(error);
        SPUtil.put(ERROR_SET, set);
    }

    public static Set<String> getError() {
        return SPUtil.get(ERROR_SET, new HashSet<>());
    }
}

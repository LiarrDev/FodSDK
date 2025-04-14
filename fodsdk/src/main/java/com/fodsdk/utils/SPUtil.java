package com.fodsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fodsdk.FodBaseApplication;

import java.util.Set;

public class SPUtil {

    private static final String SP_NAME = "fod_sdk_cache";

    public static void put(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    public static String get(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public static void put(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static int get(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static void put(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static boolean get(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static void put(String key, Set<String> set) {
        getSharedPreferences().edit().putStringSet(key, set).commit();
    }

    public static Set<String> get(String key, Set<String> set) {
        return getSharedPreferences().getStringSet(key, set);
    }

    private static SharedPreferences getSharedPreferences() {
        return FodBaseApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }
}

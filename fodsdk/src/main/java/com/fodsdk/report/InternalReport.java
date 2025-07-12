package com.fodsdk.report;

import android.app.Activity;
import android.content.Context;

import com.fodsdk.utils.LogUtil;

import java.util.Map;

class InternalReport implements IReport {

    @Override
    public void onApplication(Context context) {
        LogUtil.v("Report onApplication");
    }

    @Override
    public void onCreate(Activity activity) {
        LogUtil.v("Report onCreate");
    }

    @Override
    public void onStart(Activity activity) {
        LogUtil.v("Report onStart");
    }

    @Override
    public void onResume(Activity activity) {
        LogUtil.v("Report onResume");
    }

    @Override
    public void onPause(Activity activity) {
        LogUtil.v("Report onPause");
    }

    @Override
    public void onStop(Activity activity) {
        LogUtil.v("Report onStop");
    }

    @Override
    public void onDestroy(Activity activity) {
        LogUtil.v("Report onDestroy");
    }

    @Override
    public void onRestart(Activity activity) {
        LogUtil.v("Report onRestart");
    }

    @Override
    public void onInit(Activity activity) {
        LogUtil.v("Report onInit");
    }

    @Override
    public void onRegisterEvent(Context context, Map<String, String> map) {
        LogUtil.v("Report onRegisterEvent: " + map.toString());
    }

    @Override
    public void onPayEvent(Context context, Map<String, String> map) {
        LogUtil.v("Report onPayEvent: " + map.toString());
    }

    @Override
    public void onCustomEvent(Context context, Map<String, String> map) {
        LogUtil.v("Report onCustomEvent: " + map.toString());
    }
}
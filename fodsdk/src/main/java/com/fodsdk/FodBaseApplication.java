package com.fodsdk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.fodsdk.net.FodNet;

/**
 * SDK 内部 Application，
 * 对 CP 和渠道应使用 FodApplication
 */
public class FodBaseApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FodNet.init(context);
    }

    public static Context getContext() {
        return context;
    }

}

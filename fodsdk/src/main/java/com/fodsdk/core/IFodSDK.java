package com.fodsdk.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.fodsdk.entities.FodPayEntity;

public interface IFodSDK {

    void login(Activity activity);

    void logout(Activity activity);

    void exit(Activity activity);

    void release(Activity activity);

    void pay(Activity activity, FodPayEntity payEntity);

    void onInitSuccess(IPlatformCallback callback);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onRestart();

    void onNewIntent(Intent intent);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onConfigurationChanged(Configuration newConfig);

    void onWindowFocusChanged(boolean hasFocus);
}

package com.fodsdk.core;

import android.app.Activity;
import android.content.Intent;

import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;

public interface IGame {

    void login(Activity activity);

    void logout(Activity activity);

    void pay(Activity activity, FodPayEntity pay);

    void event(String event, FodRole role);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);

    void onWindowFocusChanged(boolean hasFocus);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}

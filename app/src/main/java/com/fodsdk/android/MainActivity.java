package com.fodsdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fodsdk.android.databinding.ActivityMainBinding;
import com.fodsdk.core.FodSDK;
import com.fodsdk.core.IPlatformCallback;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.btnInit.setOnClickListener(view -> FodSDK.get().init(this, new IPlatformCallback() {
            @Override
            public void onInit(boolean success, Bundle bundle) {
                Log.d(TAG, "Init: " + success);
            }

            @Override
            public void onLogin(int code, Bundle bundle) {

            }

            @Override
            public void onPay(int code, Bundle bundle) {

            }

            @Override
            public void onLogout(int code, Bundle bundle) {

            }
        }));
        binding.btnLogin.setOnClickListener(view -> FodSDK.get().login());
        binding.btnLogout.setOnClickListener(view -> FodSDK.get().logout());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
}
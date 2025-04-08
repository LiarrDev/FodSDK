package com.fodsdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fodsdk.core.FodSDK;
import com.fodsdk.core.IPlatformCallback;
import com.fodsdk.ui.FodUserCenterDialog;
import com.fodsdk.ui.view.FodFloatingBall;

public class MainActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_init).setOnClickListener(this);
        findViewById(R.id.btn_user_center).setOnClickListener(this);
        findViewById(R.id.btn_float_ball).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_init) {
            FodSDK.get().init(this, new IPlatformCallback() {
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
            });
        } else if (view.getId() == R.id.btn_login) {
            FodSDK.get().login();
        } else if (view.getId() == R.id.btn_user_center) {
        } else if (view.getId() == R.id.btn_float_ball) {
            FodFloatingBall ball = new FodFloatingBall();
            ball.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FodUserCenterDialog dialog = new FodUserCenterDialog(v.getContext());
                    dialog.show();
                }
            });
            ball.show();
        }
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
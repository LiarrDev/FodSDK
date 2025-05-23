package com.fodsdk.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.ResourceUtil;

public class FodUserCenterDialog extends AlertDialog {

    public FodUserCenterDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(getContext(), ResourceUtil.getLayoutId("fod_dialog_user_center"), null);
        setContentView(rootView);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = window.getAttributes();
            if (DeviceUtil.isPortrait()) {      // 竖屏
                window.setGravity(Gravity.BOTTOM);
                params.height = DeviceUtil.getScreenHeight() / 4 * 3;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
            } else {        // 横屏
                window.setGravity(Gravity.START);
                params.width = DeviceUtil.getScreenWidth() / 2;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
            }
        }

        ImageButton ibBack = rootView.findViewById(ResourceUtil.getViewId("btn_back"));
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        Window window = getWindow();
        if (window != null) {
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }
}

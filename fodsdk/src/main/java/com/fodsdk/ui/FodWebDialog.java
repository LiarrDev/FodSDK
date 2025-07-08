package com.fodsdk.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.fodsdk.core.FodSDK;
import com.fodsdk.utils.ActivityUtil;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;

public class FodWebDialog extends AlertDialog {

    private final String url;

    public FodWebDialog(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(getContext(), ResourceUtil.getLayoutId("fod_dialog_web"), null);
        setContentView(rootView);
        resize();

        WebView webView = rootView.findViewById(ResourceUtil.getViewId("web_view"));
        webView.addJavascriptInterface(new WebInterface(), "Android");
        LogUtil.v("FodWebDialog url: " + url);
        webView.loadUrl(url);
    }

    private void resize() {
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

    private class WebInterface {

        @JavascriptInterface
        public void dismissDialog() {
            LogUtil.d("WebInterface dismissDialog");
            dismiss();
        }

        @JavascriptInterface
        public void switchAccount() {
            LogUtil.d("WebInterface switchAccount");
            dismiss();
            Activity activity;
            if (getContext() instanceof Activity) {
                activity = ((Activity) getContext());
            } else {
                activity = ActivityUtil.getTopActivity();
            }
            FodSDK.get().logout(activity);
        }
    }
}

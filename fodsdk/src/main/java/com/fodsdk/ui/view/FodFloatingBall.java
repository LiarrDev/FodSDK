package com.fodsdk.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.ResourceUtil;

public class FodFloatingBall implements IFloatingView {

    private View floatingRoot;
    private ImageView floatingIcon;
    private boolean isShowing = false;
    private View.OnClickListener listener;

    @Override
    public void show(Activity activity) {
        if (activity != null && !isShowing) {
            WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            floatingRoot = LayoutInflater.from(activity).inflate(ResourceUtil.getLayoutId("fod_floating_ball"), null);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.y = DeviceUtil.getScreenHeight() / 5;
            floatingRoot.setOnTouchListener(new FloatingViewTouchListener(layoutParams, windowManager));
            windowManager.addView(floatingRoot, layoutParams);
            floatingRoot.setOnClickListener(listener);
            isShowing = true;
        }
    }

    @Override
    public void hide(Activity activity) {
        if (floatingRoot != null) {
            if (activity != null) {
                WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                windowManager.removeView(floatingRoot);
                isShowing = false;
            }
        }
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
        if (floatingRoot != null) {
            floatingRoot.setOnClickListener(listener);
        }
    }
}

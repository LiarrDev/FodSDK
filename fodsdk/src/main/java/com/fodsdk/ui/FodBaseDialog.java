package com.fodsdk.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.fodsdk.utils.ResourceUtil;

public abstract class FodBaseDialog extends AlertDialog {

    public FodBaseDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(getContext(), ResourceUtil.getLayoutId(getLayoutName()), null);
        setContentView(rootView);
        initViews(rootView);
    }

    protected abstract void initViews(View rootView);

    protected abstract String getLayoutName();

    @Override
    public void show() {
        Window window = getWindow();
        if (window != null) {
            focusNotAle(window);
        }
        super.show();
        if (window != null) {
            // AlertDialog EditText 不弹键盘，添加这两行
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            hideNavigationBar(window);
            clearFocusNotAle(window);
        }
    }

    /**
     * 隐藏虚拟栏 ，显示的时候再隐藏掉
     */
    public void hideNavigationBar(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION    // 布局位于状态栏下方
                    | View.SYSTEM_UI_FLAG_FULLSCREEN    // 全屏
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   // 隐藏导航栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else {
                uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            window.getDecorView().setSystemUiVisibility(uiOptions);
        });
    }

    /**
     * dialog 需要全屏的时候用，和clearFocusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     */
    public void focusNotAle(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * dialog 需要全屏的时候用，focusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     */
    public void clearFocusNotAle(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    protected void hideSoftKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

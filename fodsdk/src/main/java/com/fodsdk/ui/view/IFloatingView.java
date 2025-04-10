package com.fodsdk.ui.view;

import android.app.Activity;

public interface IFloatingView {

    void show(Activity activity);

    void hide(Activity activity);

    boolean isShowing();
}

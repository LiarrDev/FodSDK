package com.fodsdk.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.fodsdk.FodBaseApplication;

public class ToastUtil {
    public static void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(FodBaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

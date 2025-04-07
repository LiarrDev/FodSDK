package com.fodsdk.utils;

import android.widget.Toast;

import com.fodsdk.FodBaseApplication;

public class ToastUtil {
    public static void show(String msg) {
        Toast.makeText(FodBaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

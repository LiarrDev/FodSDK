package com.fodsdk.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.fodsdk.FodBaseApplication;

public class ToastUtil {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(FodBaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FodBaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

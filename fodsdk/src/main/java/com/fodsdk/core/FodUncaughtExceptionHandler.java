package com.fodsdk.core;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class FodUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;
    private Thread.UncaughtExceptionHandler handler;
    private static final FodUncaughtExceptionHandler instance = new FodUncaughtExceptionHandler();

    private FodUncaughtExceptionHandler() {
    }

    public static FodUncaughtExceptionHandler get() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && handler != null) {
            handler.uncaughtException(thread, throwable);
        }
    }

    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        // TODO: 收集错误信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "很抱歉，程序出现异常，即将退出…", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}

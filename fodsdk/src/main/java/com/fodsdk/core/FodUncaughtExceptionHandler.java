package com.fodsdk.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.fodsdk.entities.FodUser;
import com.fodsdk.settings.GlobalSettings;
import com.fodsdk.utils.LogUtil;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FodUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;
    private Thread.UncaughtExceptionHandler handler;
    private final Map<String, String> map = new HashMap<>();
    private final Gson gson = new Gson();
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
        getBuildInfo();
        saveError(throwable);
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

    private void getBuildInfo() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = String.valueOf(pi.versionCode);
                map.put("versionName", versionName);
                map.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtil.e("Error when collecting crash info.");
                e.printStackTrace();
            }
        }
    }

    private void saveError(Throwable throwable) {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = stringWriter.toString();
        map.put("exception", result);
        map.put("time", String.valueOf(System.currentTimeMillis()));
        FodUser user = FodSDK.get().getUser();
        if (user != null) {
            map.put("uid", user.getUid());
        } else {
            map.put("uid", "");
        }
        String json = gson.toJson(map);
        GlobalSettings.addError(json);
        LogUtil.e("保存崩溃日志：" + json);
    }
}

package com.fodsdk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.fodsdk.FodBaseApplication;

import java.util.List;

public class DeviceUtil {

    private static String imei = "00000000-0000-0000-0000-000000000000";
    private static String oaid = "00000000-0000-0000-0000-000000000000";

    public static void initPrivacy(Activity activity) {
        initOaId(activity);
        initImei(activity);
    }

    public static void initOaId(Context context) {
        OaIdHelper helper = new OaIdHelper(new OaIdHelper.AppIdsUpdater() {
            @Override
            public void onIdsUpdate(String id) {
                if (TextUtils.isEmpty(id)) {
                    LogUtil.e("OAID 为空");
                } else {
                    oaid = id;
                    LogUtil.i("OAID:" + id);
                }
            }
        });
        helper.getDeviceIds(context);
    }

    public static String getOaId() {
        return oaid;
    }

    public static void initImei(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return;
        }
        if (activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtil.apply(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionUtil.PermissionCallback() {
                @Override
                public void onResult(boolean isAllGranted, List<String> deniedList) {
                    if (isAllGranted) {
                        tryImei(activity);
                    }
                }
            });
        } else {
            tryImei(activity);
        }
    }

    public static void tryImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String id = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(id)) {
                LogUtil.e("IMEI 为kon");
            } else {
                imei = id;
                LogUtil.v("IMEI 不为空：" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImei() {
        return imei;
    }

    /**
     * 判断是否为竖屏
     */
    public static boolean isPortrait() {
        return FodBaseApplication.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 屏幕宽度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) FodBaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return 0;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    /**
     * 屏幕高度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) FodBaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return 0;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    /**
     * 应用宽度
     */
    public static int getAppWidth() {
        return FodBaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 应用高度
     */
    public static int getAppHeight() {
        return FodBaseApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 屏幕尺寸
     */
    public static String getScreenSize() {
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        int width = screenWidth == 0 ? getAppWidth() : screenWidth;
        int height = screenHeight == 0 ? getAppHeight() : screenHeight;
        return width + "*" + height;
    }

    /**
     * 设备型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL;
    }

    /**
     * 系统版本
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 网络运营商
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) FodBaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        String networkOperatorName = tm.getNetworkOperatorName();
        return TextUtils.isEmpty(networkOperatorName) ? "" : networkOperatorName;
    }

    /**
     * 网络情况
     */
    public static String getNetworkType() {
        Context context = FodBaseApplication.getContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            tm.getNetworkType();
        }
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        if (networkInfo == null || !cm.getBackgroundDataSetting()) {
            return "没有网络";
        }
        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "WiFi";
        }
        int subtype = networkInfo.getSubtype();
        switch (subtype) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "Unknown";
        }
    }

    /**
     * ANDROID_ID
     */
    public static String getAndroidId() {
        return Settings.System.getString(FodBaseApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

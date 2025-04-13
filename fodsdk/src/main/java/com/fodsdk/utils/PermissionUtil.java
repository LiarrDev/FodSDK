package com.fodsdk.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;

import com.fodsdk.ui.FodPermissionFragment;

import java.util.List;

public class PermissionUtil {

    private static final String TAG = "FodPermissionFragment";

    public static void apply(Activity activity, String[] permissions, PermissionCallback callback) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment existedFragment = fragmentManager.findFragmentByTag(TAG);
        FodPermissionFragment fragment;
        if (existedFragment != null) {
            fragment = (FodPermissionFragment) existedFragment;
        } else {
            fragment = new FodPermissionFragment();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fragmentManager.beginTransaction().add(fragment, TAG).commitNow();
            } else {
                fragmentManager.beginTransaction().add(fragment, TAG).commit();
//                fragmentManager.executePendingTransactions();
            }
        }
        fragment.apply(permissions, callback);
    }

    public interface PermissionCallback {
        void onResult(boolean isAllGranted, List<String> deniedList);
    }
}

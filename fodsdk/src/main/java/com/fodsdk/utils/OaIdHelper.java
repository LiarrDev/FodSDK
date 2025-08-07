package com.fodsdk.utils;

import android.content.Context;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

public class OaIdHelper implements IIdentifierListener {
    private final AppIdsUpdater updater;

    public OaIdHelper(AppIdsUpdater callback) {
        updater = callback;
    }

    @Override
    public void OnSupport(boolean isSupport, IdSupplier supplier) {
        if (supplier == null) {
            return;
        }
        String oaId = supplier.getOAID();
        if (updater != null) {
            updater.onIdsUpdate(oaId);
        }
    }

    public void getDeviceIds(Context context) {
        int code = MdidSdkHelper.InitSdk(context, true, this);
        switch (code) {
            case ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT: // 不支持的设备
                LogUtil.e("OaId Error - Device Not Support");
                break;
            case ErrorCode.INIT_ERROR_LOAD_CONFIGFILE:  // 加载配置文件出错
                LogUtil.e("OaId Error - Load Config File ERROR");
                break;
            case ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT: // 不支持的设备厂商
                LogUtil.e("OaId Error - Manufacturer not Support");
                break;
            case ErrorCode.INIT_ERROR_RESULT_DELAY: // 获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
                LogUtil.e("OaId Error - Result Delay");
                break;
            case ErrorCode.INIT_HELPER_CALL_ERROR: // 反射调用出错
                LogUtil.e("OaId Error - Reflect Error");
                break;
            default:
                LogUtil.e("OaId Error - Other Error" + code);
                break;
        }
    }

    public interface AppIdsUpdater {
        void onIdsUpdate(String id);
    }
}

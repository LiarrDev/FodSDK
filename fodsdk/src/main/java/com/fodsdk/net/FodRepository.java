package com.fodsdk.net;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.utils.AppUtil;
import com.fodsdk.utils.CipherUtil;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FodRepository {

    private final Gson gson = new Gson();
    private FodGameConfig config;

    public void init(FodGameConfig config) {
        this.config = config;
        try {
            String json = gson.toJson(config);
            JSONObject obj = new JSONObject(json);
            FodNet.post(new ApiInit(), obj, new FodNet.Callback() {
                @Override
                public void onResponse(JSONObject response) {
                    boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void accountLogin(String account, String password) {

    }

    public void accountRegister(String account, String registerPassword, String confirmPassword) {
        String rsaRegisterPassword = CipherUtil.encrypt(registerPassword);
        String rsaConfirmPassword = CipherUtil.encrypt(confirmPassword);
        try {
            String json = gson.toJson(config);
            JSONObject obj = new JSONObject(json);
            obj.put("account", account);
            obj.put("password", rsaRegisterPassword);
            obj.put("confirm_password", rsaConfirmPassword);
            packParams(obj);
            FodNet.post(new ApiRegisterByAccount(), obj, new FodNet.Callback() {
                @Override
                public void onResponse(JSONObject response) {
                    boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mobileRegister(String mobile, String sms) {
        try {
            String json = gson.toJson(config);
            JSONObject obj = new JSONObject(json);
            obj.put("phone", mobile);
            obj.put("code", sms);
            packParams(obj);
            FodNet.post(new ApiRegisterByPhone(), obj, new FodNet.Callback() {
                @Override
                public void onResponse(JSONObject response) {
                    boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSms(String mobile) {
        try {
            String json = gson.toJson(config);
            JSONObject obj = new JSONObject(json);
            obj.put("phone", mobile);
            obj.put("type", "phone_login");
            obj.put("androidid", DeviceUtil.getAndroidId());
            FodNet.post(new ApiGetMessage(), obj, new FodNet.Callback() {
                @Override
                public void onResponse(JSONObject response) {
                    boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void packParams(JSONObject json) throws JSONException {
//            obj.put("imei", DeviceUtil.getDeviceId(context));
//            obj.put("oaid", DeviceUtil.getOAID(context));
        json.put("androidid", DeviceUtil.getAndroidId());
        json.put("mno", DeviceUtil.getNetworkOperatorName());
        json.put("nm", DeviceUtil.getNetworkType());
        json.put("screen", DeviceUtil.getScreenSize());
        json.put("osver", DeviceUtil.getOsVersion());
        json.put("appver", AppUtil.getAppVersionName());
        json.put("devtype", "android");
    }
}

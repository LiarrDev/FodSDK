package com.fodsdk.net;

import android.text.TextUtils;
import android.util.Pair;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.entities.FodUser;
import com.fodsdk.ui.FodLoadingDialog;
import com.fodsdk.ui.FodTipsDialog;
import com.fodsdk.utils.ActivityUtil;
import com.fodsdk.utils.AppUtil;
import com.fodsdk.utils.CipherUtil;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.core.FodCallback;
import com.fodsdk.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FodRepository {

    private final Gson gson = new Gson();
    private FodGameConfig config;
    private FodLoadingDialog loading;

    public void init(FodGameConfig config, FodCallback<Pair<Boolean, Boolean>> callback) {
        this.config = config;
        try {
            String json = gson.toJson(config);
            Map map = gson.fromJson(json, Map.class);
            FodNet.post(new ApiInit(), map, new FodNet.Callback() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject rsp = new JSONObject(response);
                        boolean status = rsp.optBoolean("status");
                        if (status) {
                            JSONObject data = rsp.optJSONObject("data");
                            if (data == null) {
                                callback.onValue(new Pair<>(false, false));
                                return;
                            }
                            JSONObject floatWindow = data.optJSONObject("float_window_status");
                            if (floatWindow == null) {
                                callback.onValue(new Pair<>(false, false));
                                return;
                            }
                            boolean showFloating = floatWindow.optBoolean("status");
                            callback.onValue(new Pair<>(true, showFloating));
                        } else {
                            ToastUtil.show(rsp.optString("data"));
                            callback.onValue(new Pair<>(false, false));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onValue(new Pair<>(false, false));
                    }
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            callback.onValue(new Pair<>(false, false));
        }
    }

    public void accountRegister(String account, String registerPassword, String confirmPassword) {
        try {
            String rsaRegisterPassword = CipherUtil.encrypt(registerPassword);
            String rsaConfirmPassword = CipherUtil.encrypt(confirmPassword);
            String json = gson.toJson(config);
            Map<String, String> map = gson.fromJson(json, Map.class);
            map.put("account", account);
            map.put("password", rsaRegisterPassword);
            map.put("confirm_password", rsaConfirmPassword);
            packParams(map);
            showLoading();
            FodNet.post(new ApiRegisterByAccount(), map, new FodNet.Callback() {
                @Override
                public void onResponse(String response) {
                    hideLoading();
                    try {
                        JSONObject rsp = new JSONObject(response);
                        boolean status = rsp.optBoolean("status");
                        if (status) {
                            FodUser user = new FodUser();
                            JSONObject data = rsp.optJSONObject("data");
                            if (data != null) {
                                user.setAccount(data.optString("account"));
                                user.setUid(data.optString("uid"));
                                user.setToken(data.optString("token"));
                                user.setPhone(data.optString("phone"));

                                JSONObject realInfo = data.optJSONObject("real_info");
                                if (realInfo != null) {
                                    int realInfoStatus = realInfo.optInt("status");
                                    int isForceRealName = realInfo.optInt("is_force_real_name");
                                    String url = realInfo.optString("url");
                                    String msg = realInfo.optString("msg");
                                    int isRealName = realInfo.optInt("is_real_name");
                                    user.setRealName(isRealName == 1);
                                    if (!TextUtils.isEmpty(msg)) {
                                        ToastUtil.show(msg);
                                    }
                                }
                            }
                        } else {
                            ToastUtil.show(rsp.optString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            ToastUtil.show("参数错误");
            e.printStackTrace();
        }
    }

    public void mobileRegister(String mobile, String sms) {
        try {
            String json = gson.toJson(config);
            Map<String, String> map = gson.fromJson(json, Map.class);
            map.put("phone", mobile);
            map.put("code", sms);
            packParams(map);
            FodNet.post(new ApiRegisterByPhone(), map, new FodNet.Callback() {
                @Override
                public void onResponse(String response) {
                    /*boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }*/
                }
            });
        } catch (JsonSyntaxException e) {
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
            showLoading();
            FodNet.post(new ApiGetMessage(), new HashMap<>(), new FodNet.Callback() {
                @Override
                public void onResponse(String response) {
                    hideLoading();
                   /* boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }*/
                }

                @Override
                public void onError(Exception e) {
                    hideLoading();
                    FodNet.Callback.super.onError(e);
                }
            });
        } catch (JSONException e) {
            ToastUtil.show("参数错误");
            e.printStackTrace();
        }
    }

    public void accountLogin(String account, String password) {
    }

    private void showLoading() {
        loading = new FodLoadingDialog(ActivityUtil.getTopActivity());
        loading.show();
    }

    private void hideLoading() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void showTips(String tips) {
        FodTipsDialog dialog = new FodTipsDialog(ActivityUtil.getTopActivity(), tips);
        dialog.show();
    }

    private void packParams(Map<String, String> map) {
        map.put("imei", DeviceUtil.getImei());
        map.put("oaid", DeviceUtil.getOaId());
        map.put("androidid", DeviceUtil.getAndroidId());
        map.put("mno", DeviceUtil.getNetworkOperatorName());
        map.put("nm", DeviceUtil.getNetworkType());
        map.put("dev", DeviceUtil.getPhoneModel());
        map.put("screen", DeviceUtil.getScreenSize());
        map.put("osver", DeviceUtil.getOsVersion());
        map.put("appver", AppUtil.getAppVersionName());
        map.put("devtype", "android");
    }
}

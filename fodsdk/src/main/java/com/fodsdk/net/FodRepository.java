package com.fodsdk.net;

import android.net.Uri;
import android.util.Pair;

import com.fodsdk.core.FodConstants;
import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;
import com.fodsdk.entities.FodUser;
import com.fodsdk.net.api.ApiCheckOrderStatus;
import com.fodsdk.net.api.ApiEvent;
import com.fodsdk.net.api.ApiGetMessage;
import com.fodsdk.net.api.ApiGetOrderPostData;
import com.fodsdk.net.api.ApiSetPayInfo;
import com.fodsdk.net.api.ApiInit;
import com.fodsdk.net.api.ApiLogin;
import com.fodsdk.net.api.ApiRegisterByAccount;
import com.fodsdk.net.response.InitResult;
import com.fodsdk.net.response.LoginResponse;
import com.fodsdk.net.response.InitResponse;
import com.fodsdk.net.response.PayInfoResponse;
import com.fodsdk.settings.GlobalSettings;
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

    /**
     * 初始化
     *
     * @param config   fod_game_config.json 的配置
     * @param callback Pair 第一个参数为是否初始化成功，第二个参数为是否显示悬浮球
     */
    public void init(FodGameConfig config, FodCallback<InitResult> callback) {
        this.config = config;
        InitResult result = new InitResult();
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
                            result.setSuccess(true);
                            JSONObject data = rsp.optJSONObject("data");
                            if (data == null) {
                                callback.onValue(result);
                                return;
                            }
                            InitResponse initResponse = gson.fromJson(data.toString(), InitResponse.class);
                            if (initResponse != null) {
                                result.setShowFloat(initResponse.getFloatWindowStatus().isStatus());
                                result.setShowMobileLogin(initResponse.getShowPhoneStatus().isStatus());
                            }
                            callback.onValue(result);
                        } else {
                            ToastUtil.show(rsp.optString("data"));
                            callback.onValue(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onValue(result);
                    }
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            callback.onValue(result);
        }
    }

    public void accountRegister(String account, String registerPassword, String confirmPassword, FodCallback<LoginResponse> callback) {
        try {
            String rsaRegisterPassword = CipherUtil.rsa(registerPassword);
            String rsaConfirmPassword = CipherUtil.rsa(confirmPassword);
            String json = gson.toJson(config);
            Map<String, String> map = gson.fromJson(json, Map.class);
            map.put("account", account);
            map.put("password", rsaRegisterPassword);
            map.put("confirm_password", rsaConfirmPassword);
            map.putAll(getDeviceParams());
            showLoading();
            FodNet.post(new ApiRegisterByAccount(), map, new FodNet.Callback() {
                @Override
                public void onResponse(String response) {
                    hideLoading();
                    cacheAccount(response, account, registerPassword);
                    handleLogin(response, callback);
                }

                @Override
                public void onError(Exception e) {
                    FodNet.Callback.super.onError(e);
                    hideLoading();
                }
            });
        } catch (Exception e) {
            ToastUtil.show("参数错误");
            e.printStackTrace();
        }
    }

    public void accountLogin(String account, String password, FodCallback<LoginResponse> callback) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        map.put("login_type", "1");
        map.put("account", account);
        map.put("password", CipherUtil.rsa(password));
        map.putAll(getDeviceParams());
        showLoading();
        FodNet.post(new ApiLogin(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                cacheAccount(response, account, password);
                handleLogin(response, callback);
            }

            @Override
            public void onError(Exception e) {
                FodNet.Callback.super.onError(e);
                hideLoading();
            }
        });
    }

    public void tokenLogin(String token, FodCallback<Pair<Boolean, LoginResponse>> callback) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        map.put("login_type", "3");
        map.put("token", token);
        map.putAll(getDeviceParams());
        showLoading();
        FodNet.post(new ApiLogin(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                try {
                    JSONObject rsp = new JSONObject(response);
                    boolean status = rsp.optBoolean("status");
                    if (status) {
                        JSONObject data = rsp.optJSONObject("data");
                        if (data != null) {
                            LoginResponse loginResponse = gson.fromJson(data.toString(), LoginResponse.class);
                            callback.onValue(new Pair<>(true, loginResponse));
                        }
                    } else {
                        ToastUtil.show(rsp.optString("data"));
                        callback.onValue(new Pair<>(false, null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                FodNet.Callback.super.onError(e);
                hideLoading();
            }
        });
    }

    public void mobileLogin(String mobile, String sms) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        map.put("login_type", "2");
        map.put("phone", mobile);
        map.put("code", sms);
        map.putAll(getDeviceParams());
        showLoading();
        FodNet.post(new ApiLogin(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                // TODO: 接口未实现
            }

            @Override
            public void onError(Exception e) {
                FodNet.Callback.super.onError(e);
                hideLoading();
            }
        });
    }

    private void handleLogin(String response, FodCallback<LoginResponse> callback) {
        try {
            JSONObject rsp = new JSONObject(response);
            boolean status = rsp.optBoolean("status");
            if (status) {
                GlobalSettings.setLoginBefore(true);
                JSONObject data = rsp.optJSONObject("data");
                if (data != null) {
                    LoginResponse registerResponse = gson.fromJson(data.toString(), LoginResponse.class);
                    callback.onValue(registerResponse);
                }
            } else {
                ToastUtil.show(rsp.optString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cacheAccount(String response, String account, String password) {
        try {
            JSONObject rsp = new JSONObject(response);
            boolean status = rsp.optBoolean("status");
            if (status) {
                GlobalSettings.setLastLoginAccount(account);
                GlobalSettings.setLastLoginPassword(password);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSms(String mobile, FodCallback<Void> callback) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        map.put("phone", mobile);
        map.put("type", "phone_login");
        map.put("androidid", DeviceUtil.getAndroidId());
        String sign = CipherUtil.md5(config.getGId() + config.getPkId() + mobile + "fnsdfsea3@ws!#wa");
        map.put("sign", sign);
        showLoading();
        FodNet.post(new ApiGetMessage(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                try {
                    JSONObject rsp = new JSONObject(response);
                    boolean status = rsp.optBoolean("status");
                    if (status) {
                        JSONObject data = rsp.optJSONObject("data");
                        if (data != null) {
                            String msg = data.optString("msg");
                            ToastUtil.show(msg);
                            callback.onValue(null);
                        }
                    } else {
                        String data = rsp.optString("data");
                        ToastUtil.show(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                hideLoading();
                FodNet.Callback.super.onError(e);
            }
        });
    }

    public void pay(FodUser user, FodPayEntity entity, FodCallback<String> callback) {
        try {
            String json = gson.toJson(config);
            Map<String, String> map = gson.fromJson(json, Map.class);
            map.put("uid", user.getUid());
            map.put("token", user.getToken());
            map.put("gameOrder", entity.getOrderId());
            map.put("goodsId", entity.getGoodsId());
            map.put("goodsNum", String.valueOf(entity.getGoodsCount()));
            map.put("goodsName", entity.getGoodsName());
            map.put("roleLevel", String.valueOf(entity.getRole().getRoleLevel()));
            map.put("roleName", entity.getRole().getRoleName());
            map.put("roleId", entity.getRole().getRoleId());
            map.put("serverId", entity.getRole().getServerId());
            map.put("ext", entity.getExt());
            map.put("money", priceToMoney(entity.getPrice()));
            map.putAll(getDeviceParams());

            showLoading();
            FodNet.post(new ApiSetPayInfo(), map, new FodNet.Callback() {
                @Override
                public void onResponse(String response) {
                    hideLoading();
                    try {
                        JSONObject rsp = new JSONObject(response);
                        boolean status = rsp.optBoolean("status");
                        if (status) {
                            JSONObject data = rsp.optJSONObject("data");
                            if (data != null) {
                                PayInfoResponse payInfoResponse = gson.fromJson(data.toString(), PayInfoResponse.class);
                                callback.onValue(payInfoResponse.getPayToken());
                            }
                        } else {
                            ToastUtil.show(rsp.optString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {
                    FodNet.Callback.super.onError(e);
                    hideLoading();
                }
            });

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回调第一个参数为是否成功，第二个参数为是否强制停止轮询
     */
    public void getOrderStatus(String order, FodCallback<Pair<Boolean, Boolean>> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("order", order);
        FodNet.post(new ApiCheckOrderStatus(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rsp = new JSONObject(response);
                    boolean status = rsp.optBoolean("status");
                    if (status) {
                        boolean payFinish = rsp.optInt("data") == 1;
                        callback.onValue(new Pair<>(payFinish, false));
                    } else {
                        ToastUtil.show(rsp.optString("data"));
                        callback.onValue(new Pair<>(false, true));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getOrderPostData(FodUser user, String order, int price, FodCallback<String> callback) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        map.put("uid", user.getUid());
        map.put("order", order);
        map.put("money", priceToMoney(price));
        FodNet.post(new ApiGetOrderPostData(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rsp = new JSONObject(response);
                    boolean status = rsp.optBoolean("status");
                    if (status) {
                        JSONObject data = rsp.optJSONObject("data");
                        if (data != null) {
                            String money = data.optString("money");
                            callback.onValue(money);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void logEvent(String event, Map<String, String> extendMap) {
        logEvent(event, extendMap, null);
    }

    public void logEvent(String event, Map<String, String> extendMap, FodCallback<Void> callback) {
        String json = gson.toJson(config);
        Map<String, String> baseMap = gson.fromJson(json, Map.class);
        baseMap.putAll(getDeviceParams());
        String baseData = gson.toJson(baseMap);
        String extendData = gson.toJson(extendMap);
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = CipherUtil.md5(baseData + extendData + time + "sjhdhsadfeairwejn23");

        Map<String, String> map = new HashMap<>();
        map.put("type", event);
        map.put("retime", time);
        map.put("baseData", baseData);
        map.put("extendData", extendData);
        map.put("sign", sign);

        FodNet.post(new ApiEvent(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                if (callback != null) {
                    callback.onValue(null);
                }
            }
        });
    }

    public String getUserCenterUrl(FodUser user, FodRole role) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        map.putAll(getDeviceParams());
        if (user != null) {
            map.put("uid", user.getUid());
            map.put("token", user.getToken());
        }
        if (role != null) {
            map.put("roleId", role.getRoleId());
            map.put("roleName", role.getRoleName());
            map.put("roleLevel", String.valueOf(role.getRoleLevel()));
        }
        Uri.Builder builder = Uri.parse(FodConstants.Inner.URL_USER_CENTER).buildUpon();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().toString();
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

    private Map<String, String> getDeviceParams() {
        Map<String, String> map = new HashMap<>();
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
        map.put("os", "android");
        map.put("idfv", "");
        map.put("idfa", "");
        map.put("caid", "");
        return map;
    }

    private String priceToMoney(int price) {
        String priceStr = String.valueOf(price);
        String money;
        if (priceStr.length() > 2) {
            money = priceStr.substring(0, priceStr.length() - 2) + "." + priceStr.substring(priceStr.length() - 2);
        } else {
            money = "0." + (priceStr.length() == 1 ? "0" : "") + priceStr;
        }
        return money;
    }

    public FodGameConfig getGameConfig() {
        return config;
    }
}

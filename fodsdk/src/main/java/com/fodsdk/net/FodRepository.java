package com.fodsdk.net;

import android.net.Uri;
import android.util.Pair;

import com.fodsdk.core.FodConstants;
import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;
import com.fodsdk.entities.FodUser;
import com.fodsdk.net.api.ApiGetMessage;
import com.fodsdk.net.api.ApiSetPayInfo;
import com.fodsdk.net.api.ApiInit;
import com.fodsdk.net.api.ApiLogin;
import com.fodsdk.net.api.ApiRegisterByAccount;
import com.fodsdk.net.api.ApiRegisterByPhone;
import com.fodsdk.net.response.LoginResponse;
import com.fodsdk.net.response.InitResponse;
import com.fodsdk.net.response.PayInfoResponse;
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
                            InitResponse initResponse = gson.fromJson(data.toString(), InitResponse.class);
                            boolean showFloat = false;
                            if (initResponse != null) {
                                showFloat = initResponse.getFloatWindowStatus().isStatus();
                            }
                            callback.onValue(new Pair<>(true, showFloat));
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

    public void accountRegister(String account, String registerPassword, String confirmPassword, FodCallback<LoginResponse> callback) {
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
        map.put("password", CipherUtil.encrypt(password));
        packParams(map);
        showLoading();
        FodNet.post(new ApiLogin(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                hideLoading();
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
        packParams(map);
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

    private void handleLogin(String response, FodCallback<LoginResponse> callback) {
        try {
            JSONObject rsp = new JSONObject(response);
            boolean status = rsp.optBoolean("status");
            if (status) {
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
            map.put("ext", entity.getExt());
            packParams(map);

            String price = String.valueOf(entity.getPrice());
            String money;
            if (price.length() > 2) {
                money = price.substring(0, price.length() - 2) + "." + price.substring(price.length() - 2);
            } else {
                money = "0." + (price.length() == 1 ? "0" : "") + price;
            }
            map.put("money", money);

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

    public String getUserCenterUrl(FodRole role) {
        String json = gson.toJson(config);
        Map<String, String> map = gson.fromJson(json, Map.class);
        packParams(map);
        if (role != null) {
            map.put("roleId", role.getRoleId());
            map.put("roleName", role.getRoleName());
            map.put("roleLevel", String.valueOf(role.getRoleLevel()));
        }
        Uri.Builder builder = Uri.parse(FodConstants.FOD_USER_CENTER).buildUpon();
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
        map.put("os", "android");
    }

    public FodGameConfig getGameConfig() {
        return config;
    }
}

package com.fodsdk.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;
import com.fodsdk.entities.FodUser;
import com.fodsdk.net.FodRepository;
import com.fodsdk.net.response.InitResult;
import com.fodsdk.net.response.LoginRealInfo;
import com.fodsdk.net.response.LoginResponse;
import com.fodsdk.report.FodReport;
import com.fodsdk.settings.GlobalSettings;
import com.fodsdk.settings.UserSettings;
import com.fodsdk.ui.FodExitDialog;
import com.fodsdk.ui.FodLoginDialog;
import com.fodsdk.ui.FodPayDialog;
import com.fodsdk.ui.FodWebDialog;
import com.fodsdk.ui.view.FodFloatingBall;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;
import com.fodsdk.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class FodSDKCore implements IFodSDK {

    protected Activity activity;
    private final FodRepository repo = new FodRepository();
    private final Gson gson = new Gson();
    protected IPlatformCallback platformCallback;
    public FodGameConfig config;
    private FodUser user;
    private FodRole role;
    private FodFloatingBall ball = new FodFloatingBall();
    private boolean showFloatingBall = false;
    private boolean showMobileLogin = false;
    private FodHeartBeat heartBeat;
    private final List<Pair<String, FodPayResultPollingTask>> tasks = new ArrayList<>();

    public synchronized void init(Activity activity, IPlatformCallback callback) {
        LogUtil.v("init");
        this.activity = activity;
        this.platformCallback = callback;
        initConfig();
        DeviceUtil.initPrivacy(activity, new FodCallback<Void>() {
            @Override
            public void onValue(Void unused) {
                repo.init(config, new FodCallback<InitResult>() {
                    @Override
                    public void onValue(InitResult initResult) {
                        callback.onInit(initResult.isSuccess() ? FodConstants.Code.SUCCESS : FodConstants.Code.FAILURE, new Bundle());
                        showFloatingBall = initResult.isShowFloat();
                        showMobileLogin = initResult.isShowMobileLogin();
                    }
                });
                logEvent(FodConstants.Event.SCENE_OPEN, null);
                uploadError();
                FodReport.get().onInit(activity);
            }
        });
        FodReport.get().onCreate(activity);
    }

    @Override
    public void login(Activity activity) {
        FodLoginDialog dialog = new FodLoginDialog(activity, repo, showMobileLogin);
        FodCallback<LoginResponse> callback = new FodCallback<LoginResponse>() {
            @Override
            public void onValue(LoginResponse response) {
                user = new FodUser();
                user.setAccount(response.getAccount());
                user.setUid(response.getUid());
                user.setToken(response.getToken());
                user.setPhone(response.getPhone());
                user.setRealName(response.getRealInfo().getIsRealName() == 1);

                GlobalSettings.setLastLoginToken(user.getToken());
                if (response.getIsNewRegister() == 1) {
                    FodReport.get().onRegisterEvent(activity, new HashMap<>());
                }

                // 回调给 CP
                Bundle bundle = new Bundle();
                bundle.putString("uid", user.getUid());
                bundle.putString("token", user.getToken());
                platformCallback.onLogin(FodConstants.Code.SUCCESS, bundle);

                if (showFloatingBall) {
                    ball.show(activity);
                    ball.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showUserCenter(activity);
                        }
                    });
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                showRealNameDialog(activity, response);

                logEvent(FodConstants.Event.SCENE_LOGIN, null);
                heartBeat = new FodHeartBeat() {
                    @Override
                    FodRole getRole() {
                        return role;
                    }
                };
                heartBeat.start();
            }
        };
        String token = GlobalSettings.getLastLoginToken();
        if (TextUtils.isEmpty(token)) {
            dialog.setOnLoginCallback(callback);
            dialog.show();
        } else {
            repo.tokenLogin(token, new FodCallback<Pair<Boolean, LoginResponse>>() {
                @Override
                public void onValue(Pair<Boolean, LoginResponse> booleanLoginResponsePair) {
                    if (booleanLoginResponsePair.first) {
                        callback.onValue(booleanLoginResponsePair.second);
                    } else {
                        dialog.setOnLoginCallback(callback);
                        dialog.show();
                    }
                }
            });
        }
    }

    @Override
    public void logout(Activity activity) {
        release(activity);
        GlobalSettings.setLastLoginToken("");
        platformCallback.onLogout(FodConstants.Code.SUCCESS, new Bundle());
    }

    @Override
    public void pay(Activity activity, FodPayEntity entity) {
        repo.pay(user, entity, new FodCallback<String>() {
            @Override
            public void onValue(String payToken) {
                FodPayDialog dialog = new FodPayDialog(activity, payToken, new FodCallback<String>() {
                    @Override
                    public void onValue(String order) {
                        checkPayResult(order, entity);
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void exit(Activity activity) {
        FodExitDialog dialog = new FodExitDialog(activity, new FodCallback<Boolean>() {
            @Override
            public void onValue(Boolean exit) {
                if (exit) {
                    release(activity);
                    platformCallback.onExit(FodConstants.Code.SUCCESS, new Bundle());
                } else {
                    platformCallback.onExit(FodConstants.Code.FAILURE, new Bundle());
                }
            }
        });
        dialog.show();
    }

    @Override
    public void release(Activity activity) {
        ball.hide(activity);
        user = null;
        role = null;
        for (Pair<String, FodPayResultPollingTask> pair : tasks) {
            pair.second.stopPolling();
        }
        tasks.clear();
        heartBeat.stop();
    }

    @Override
    public void onInitSuccess(IPlatformCallback callback) {
    }

    @Override
    public void onStart() {
        FodReport.get().onStart(activity);
    }

    @Override
    public void onResume() {
        FodReport.get().onResume(activity);
    }

    @Override
    public void onPause() {
        FodReport.get().onPause(activity);
    }

    @Override
    public void onStop() {
        FodReport.get().onStop(activity);
    }

    @Override
    public void onDestroy() {
        FodReport.get().onDestroy(activity);
    }

    @Override
    public void onRestart() {
        FodReport.get().onRestart(activity);
    }

    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }

    private void initConfig() {
        try {
            config = gson.fromJson(ResourceUtil.readAssets2String(FodConstants.Inner.GAME_CONFIG_FILE), FodGameConfig.class);
            LogUtil.v("config: " + config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showUserCenter(Activity activity) {
        String url = repo.getUserCenterUrl(user, role);
        FodWebDialog dialog = new FodWebDialog(activity, url);
        dialog.show();
    }

    private void showRealNameDialog(Activity activity, LoginResponse response) {
        if (true) {
            return; // TODO: test, remove this later
        }
        LoginRealInfo realInfo = response.getRealInfo();
        if (realInfo.getIsRealName() == 0) {
            UserSettings.setTodayAllowPlayTime(realInfo.getTime());
            String msg = realInfo.getMsg();
            ToastUtil.show(msg);

            FodWebDialog dialog = new FodWebDialog(activity, realInfo.getUrl());
            if (realInfo.getIsForceRealName() == 1) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
            dialog.show();
        }
    }

    private void checkPayResult(String order, FodPayEntity entity) {
        FodPayResultPollingTask task = new FodPayResultPollingTask(repo, order, new FodCallback<Void>() {
            @Override
            public void onValue(Void unused) {
                getOrderPostData(order, entity);
                removeTask(order);
            }
        });
        tasks.add(new Pair<>(order, task));
        task.startPolling();
    }

    private synchronized void removeTask(String order) {
        int index = -1;
        for (Pair<String, FodPayResultPollingTask> pair : tasks) {
            if (pair.first.equals(order)) {
                index = tasks.indexOf(pair);
                pair.second.stopPolling();
            }
        }
        if (index != -1) {
            tasks.remove(index);
        }
    }

    private void getOrderPostData(String order, FodPayEntity entity) {
        if (user == null) {
            return;
        }
        repo.getOrderPostData(user, order, entity.getPrice(), new FodCallback<String>() {
            @Override
            public void onValue(String money) {
                Map<String, String> map = new HashMap<>();
                map.put("money", money);
                FodReport.get().onPayEvent(activity, map);
            }
        });
    }

    public void logEvent(String event, FodRole role) {
        Map<String, String> map = new HashMap<>();
        if (user != null) {
            map.put("uid", user.getUid());
        }
        switch (event) {
            case FodConstants.Event.SCENE_ENTRY:
            case FodConstants.Event.SCENE_CREATE_ROLE:
            case FodConstants.Event.SCENE_LEVEL:
                if (role != null) {
                    this.role = role;
                    map.put("serverId", role.getServerId());
                    map.put("roleId", role.getRoleId());
                    map.put("roleName", role.getRoleName());
                    map.put("roleLevel", String.valueOf(role.getRoleLevel()));
                }
                break;
            case FodConstants.Event.SCENE_ONLINE:
                map.put("serverId", role == null ? "" : role.getServerId());
                map.put("roleId", role == null ? "" : role.getRoleId());
                map.put("roleName", role == null ? "" : role.getRoleName());
                map.put("roleLevel", role == null ? "" : String.valueOf(role.getRoleLevel()));
            default:
                break;
        }
        repo.logEvent(event, map);

        Map<String, String> reportMap = new HashMap<>(map);
        reportMap.put("event", event);
        FodReport.get().onCustomEvent(activity, reportMap);
    }

    private void uploadError() {
        LogUtil.d("uploadError");
        Set<String> set = GlobalSettings.getError();
        LogUtil.d("error num: " + set.size());
        for (String error : set) {
            Map<String, String> map = new HashMap<>();
            try {
                JSONObject json = new JSONObject(error);
                String uid = json.optString("uid");
                LogUtil.e("uid: " + uid);
                map.put("uid", uid);
                map.put("error", error);
                repo.logEvent(FodConstants.Event.SCENE_ERROR, map, new FodCallback<Void>() {
                    @Override
                    public void onValue(Void unused) {
                        GlobalSettings.removeError(error);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public FodUser getUser() {
        return user;
    }
}

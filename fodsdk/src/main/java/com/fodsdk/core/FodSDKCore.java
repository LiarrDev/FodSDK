package com.fodsdk.core;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;
import com.fodsdk.entities.FodUser;
import com.fodsdk.net.FodRepository;
import com.fodsdk.net.response.LoginRealInfo;
import com.fodsdk.net.response.LoginResponse;
import com.fodsdk.settings.GlobalSettings;
import com.fodsdk.settings.UserSettings;
import com.fodsdk.ui.FodLoginDialog;
import com.fodsdk.ui.FodPayDialog;
import com.fodsdk.ui.FodWebDialog;
import com.fodsdk.ui.view.FodFloatingBall;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;
import com.fodsdk.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public abstract class FodSDKCore {

    protected Activity activity;
    private final FodRepository repo = new FodRepository();
    private final Gson gson = new Gson();
    protected IPlatformCallback platformCallback;
    public FodGameConfig config;
    private FodUser user;
    private FodRole role;
    private FodFloatingBall ball = new FodFloatingBall();
    private boolean showFloatingBall = false;
    private FodHeartBeat heartBeat;

    public synchronized void init(Activity activity, IPlatformCallback callback) {
        LogUtil.v("init");
        this.activity = activity;
        this.platformCallback = callback;

        /*boolean firstLaunch = GlobalSettings.isFirstLaunch();
        if (firstLaunch) {
            FodAgreementDialog dialog = new FodAgreementDialog(activity);
            dialog.show();
        } else {
        }*/
        initConfig();
        DeviceUtil.initPrivacy(activity);
        repo.init(config, new FodCallback<Pair<Boolean, Boolean>>() {
            @Override
            public void onValue(Pair<Boolean, Boolean> pair) {
                callback.onInit(pair.first ? FodConstants.Code.SUCCESS : FodConstants.Code.FAILURE, new Bundle());
                showFloatingBall = pair.second;
            }
        });
        logEvent(FodConstants.Event.SCENE_OPEN, null);
    }


    private void requestPermissions() {
        // TODO
        GlobalSettings.setFirstLaunch(false);
    }

    private void initConfig() {
        try {
            config = gson.fromJson(ResourceUtil.readAssets2String(FodConstants.Inner.GAME_CONFIG_FILE), FodGameConfig.class);
            LogUtil.v("config: " + config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(Activity activity) {
        FodLoginDialog dialog = new FodLoginDialog(activity, repo);
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
                heartBeat = new FodHeartBeat(user) {
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

    private void showUserCenter(Activity activity) {
        String url = repo.getUserCenterUrl(role);
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

    public void logout(Activity activity) {
        ball.hide(activity);
        user = null;
        role = null;
        heartBeat.stop();
        GlobalSettings.setLastLoginToken("");
        platformCallback.onLogout(FodConstants.Code.SUCCESS, new Bundle());
    }

    public void pay(Activity activity, FodPayEntity entity) {
        repo.pay(user, entity, new FodCallback<String>() {
            @Override
            public void onValue(String payToken) {
                FodPayDialog dialog = new FodPayDialog(activity, payToken);
                dialog.show();
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
    }

    public FodUser getUser() {
        return user;
    }
}

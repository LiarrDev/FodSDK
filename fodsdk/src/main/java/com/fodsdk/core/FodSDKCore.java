package com.fodsdk.core;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.entities.FodUser;
import com.fodsdk.net.FodRepository;
import com.fodsdk.net.response.AccountRegisterRealInfo;
import com.fodsdk.net.response.AccountRegisterResponse;
import com.fodsdk.settings.GlobalSettings;
import com.fodsdk.settings.UserSettings;
import com.fodsdk.ui.FodLoginDialog;
import com.fodsdk.ui.FodWebDialog;
import com.fodsdk.ui.view.FodFloatingBall;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;
import com.fodsdk.utils.ToastUtil;
import com.google.gson.Gson;

public abstract class FodSDKCore {

    protected Activity activity;
    private final FodRepository repo = new FodRepository();
    private final Gson gson = new Gson();
    protected IPlatformCallback platformCallback;
    public FodGameConfig config;
    private FodUser user;
    private FodFloatingBall ball = new FodFloatingBall();
    private boolean showFloatingBall = false;

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
                callback.onInit(pair.first, new Bundle());
                showFloatingBall = pair.second;
            }
        });
    }


    private void requestPermissions() {
        // TODO
        GlobalSettings.setFirstLaunch(false);
    }

    private void initConfig() {
        try {
            config = gson.fromJson(ResourceUtil.readAssets2String(FodConstants.FOD_GAME_CONFIG_FILE), FodGameConfig.class);
            LogUtil.v("config: " + config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login() {
        FodLoginDialog dialog = new FodLoginDialog(activity, repo);
        dialog.setOnRegisterCallback(new FodCallback<AccountRegisterResponse>() {
            @Override
            public void onValue(AccountRegisterResponse response) {
                user = new FodUser();
                user.setAccount(response.getAccount());
                user.setUid(response.getUid());
                user.setToken(response.getToken());
                user.setPhone(response.getPhone());
                user.setRealName(response.getRealInfo().getIsRealName() == 1);

                // 回调给 CP
                Bundle bundle = new Bundle();
                bundle.putString("uid", user.getUid());
                bundle.putString("token", user.getToken());
                platformCallback.onLogin(FodConstants.Code.SUCCESS, bundle);

                ball.show(activity);

                dialog.dismiss();

                showRealNameDialog(response);
            }
        });
        dialog.show();
    }

    private void showRealNameDialog(AccountRegisterResponse response) {
        AccountRegisterRealInfo realInfo = response.getRealInfo();
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

    public void logout() {

    }

    public void pay() {

    }

    public FodUser getUser() {
        return user;
    }
}

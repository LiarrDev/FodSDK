package com.fodsdk.core;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.net.FodRepository;
import com.fodsdk.settings.GlobalSettings;
import com.fodsdk.ui.FodLoginDialog;
import com.fodsdk.ui.view.FodFloatingBall;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;
import com.google.gson.Gson;

public abstract class FodSDKCore {

    protected Activity activity;
    protected IPlatformCallback callback;
    public FodGameConfig config;
    private final FodRepository repo = new FodRepository();
    private final Gson gson = new Gson();
    private FodFloatingBall ball = new FodFloatingBall();
    private boolean showFloatingBall = false;

    public synchronized void init(Activity activity, IPlatformCallback callback) {
        LogUtil.v("init");
        this.activity = activity;
        this.callback = callback;

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
        dialog.show();
    }

    public void logout() {

    }

    public void pay() {

    }
}

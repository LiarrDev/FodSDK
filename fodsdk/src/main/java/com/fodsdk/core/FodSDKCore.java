package com.fodsdk.core;

import android.app.Activity;
import android.os.Bundle;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.net.FodRepository;
import com.fodsdk.settings.GlobalSettings;
import com.fodsdk.ui.FodAgreementDialog;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;
import com.google.gson.Gson;

public abstract class FodSDKCore {

    protected Activity activity;
    protected IPlatformCallback callback;
    public FodGameConfig config;
    private final FodRepository repo = new FodRepository();
    private final Gson gson = new Gson();


    public synchronized void init(Activity activity, IPlatformCallback callback) {
        LogUtil.v("init");
        this.activity = activity;
        this.callback = callback;

        boolean firstLaunch = GlobalSettings.isFirstLaunch();
        if (firstLaunch) {
            FodAgreementDialog dialog = new FodAgreementDialog(activity);
            dialog.show();
        } else {

        }
        initConfig();

        if (config != null) {
            this.callback.onInit(FodConstants.Code.SUCCESS, new Bundle());  // TODO: 所有配置加载成功后
        }
    }


    private void requestPermissions() {
        // TODO
        GlobalSettings.setFirstLaunch(false);
    }

    private void initConfig() {
        try {
            config = gson.fromJson(ResourceUtil.readAssets2String(FodConstants.FOD_GAME_CONFIG_FILE), FodGameConfig.class);
            LogUtil.e("config: " + config);
            repo.init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login() {

    }

    public void logout() {

    }

    public void pay() {

    }
}

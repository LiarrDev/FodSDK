package com.fodsdk.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.fodsdk.android.demo.databinding.ActivityMainBinding;
import com.fodsdk.core.FodConstants;
import com.fodsdk.core.FodSDK;
import com.fodsdk.core.IPlatformCallback;
import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;
import com.fodsdk.utils.DeviceUtil;
import com.fodsdk.utils.LogUtil;
import com.hihonor.ads.identifier.AdvertisingIdClient;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.btnInit.setOnClickListener(view -> FodSDK.get().init(this, new IPlatformCallback() {
            @Override
            public void onInit(int code, Bundle bundle) {
                if (code == FodConstants.Code.SUCCESS) {
                    Log.d(TAG, "Init Success");
                }
            }

            @Override
            public void onLogin(int code, Bundle bundle) {
                if (code == FodConstants.Code.SUCCESS) {
                    String uid = bundle.getString("uid");
                    String token = bundle.getString("token");
                    Log.d(TAG, "Login Success, uid: " + uid + ", token: " + token);
                }
            }

            @Override
            public void onPay(int code, Bundle bundle) {
                if (code == FodConstants.Code.SUCCESS) {
                    Log.d(TAG, "Pay Success");
                }
            }

            @Override
            public void onLogout(int code, Bundle bundle) {
                if (code == FodConstants.Code.SUCCESS) {
                    Log.d(TAG, "Logout Success");
                }
            }

            @Override
            public void onExit(int code, Bundle bundle) {
                if (code == FodConstants.Code.SUCCESS) {
                    Log.d(TAG, "Exit Success");
                    finish();
                }
            }
        }));
        binding.btnLogin.setOnClickListener(view -> FodSDK.get().login(this));
        binding.btnLogout.setOnClickListener(view -> FodSDK.get().logout(this));
        binding.btnPay.setOnClickListener(v -> {
            String priceStr = binding.etPrice.getText().toString();
            int price = TextUtils.isEmpty(priceStr) ? 1 : Integer.parseInt(priceStr);
            FodPayEntity payEntity = new FodPayEntity();
            payEntity.setOrderId(String.valueOf(System.currentTimeMillis()));
            payEntity.setGoodsId("101");
            payEntity.setGoodsCount(1);
            payEntity.setGoodsName("商品名称");
            payEntity.setGoodsDesc("商品描述");
            payEntity.setPrice(price);
            payEntity.setExt("");
            payEntity.setRole(getRole());
            FodSDK.get().pay(this, payEntity);
        });
        binding.btnExit.setOnClickListener(view -> FodSDK.get().exit(this));
        binding.btnSelectServer.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_BEFORE_ENTRY, null));
        binding.btnEnterServer.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_ENTRY, getRole()));
        binding.btnRoleCreate.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_CREATE_ROLE, getRole()));
        binding.btnRoleLevelUp.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_LEVEL, getRole()));
        binding.btnOaId.setOnClickListener(view -> new Thread(() -> {
            String device = DeviceUtil.getPhoneModel();
            try {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                if (null != info) {
                    LogUtil.i("getAdvertisingIdInfo id=" + info.id + ", isLimitAdTrackingEnabled=" + info.isLimit);
                    runOnUiThread(() -> binding.tvOaId.setText("【" + device + "】OAID: " + info.id + ", isLimit: " + info.isLimit));

                }
            } catch (Exception e) {
                LogUtil.i("getAdvertisingIdInfo Exception: " + e.toString());
                runOnUiThread(() -> binding.tvOaId.setText("【" + device + "】OAID: " + e.toString()));
            }
        }).start());
    }

    private FodRole getRole() {
        FodRole role = new FodRole();
        role.setRoleId("AA1234");
        role.setRoleLevel(1);
        role.setRoleName("角色名称");
        role.setServerId("Server101");
        role.setServerName("服务器名称");
        return role;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FodSDK.get().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FodSDK.get().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FodSDK.get().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FodSDK.get().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FodSDK.get().onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FodSDK.get().onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FodSDK.get().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FodSDK.get().onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FodSDK.get().onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        FodSDK.get().onWindowFocusChanged(hasFocus);
    }
}
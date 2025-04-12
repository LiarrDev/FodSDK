package com.fodsdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fodsdk.android.databinding.ActivityMainBinding;
import com.fodsdk.core.FodConstants;
import com.fodsdk.core.FodSDK;
import com.fodsdk.core.IPlatformCallback;
import com.fodsdk.entities.FodPayEntity;
import com.fodsdk.entities.FodRole;

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

            }

            @Override
            public void onLogout(int code, Bundle bundle) {
                if (code == FodConstants.Code.SUCCESS) {
                    Log.d(TAG, "Logout Success");
                }
            }
        }));
        binding.btnLogin.setOnClickListener(view -> FodSDK.get().login(this));
        binding.btnLogout.setOnClickListener(view -> FodSDK.get().logout(this));
        binding.btnPay.setOnClickListener(v -> {
            FodPayEntity payEntity = new FodPayEntity();
            payEntity.setOrderId(String.valueOf(System.currentTimeMillis()));
            payEntity.setGoodsId("101");
            payEntity.setGoodsCount(1);
            payEntity.setGoodsName("商品名称");
            payEntity.setGoodsDesc("商品描述");
            payEntity.setPrice(100);
            payEntity.setExt("");
            payEntity.setRole(getRole());
            FodSDK.get().pay(this, payEntity);
        });
        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
        binding.btnSelectServer.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_BEFORE_ENTRY, null));
        binding.btnEnterServer.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_ENTRY, getRole()));
        binding.btnRoleCreate.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_CREATE_ROLE, getRole()));
        binding.btnRoleLevelUp.setOnClickListener(view -> FodSDK.get().logEvent(FodConstants.Event.SCENE_LEVEL, getRole()));
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
    }
}
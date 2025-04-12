package com.fodsdk.core;

import android.os.Handler;
import android.os.Looper;

import com.fodsdk.entities.FodRole;
import com.fodsdk.entities.FodUser;
import com.fodsdk.net.FodNet;
import com.fodsdk.net.api.ApiCheckLogin;
import com.fodsdk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public abstract class FodHeartBeat {

    private static final long HEARTBEAT_INTERVAL_5 = 5 * 60 * 1000;
    private static final long HEARTBEAT_INTERVAL_10 = 10 * 60 * 1000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable5Min = new Runnable() {
        @Override
        public void run() {
            reportOnline();
            handler.postDelayed(this, HEARTBEAT_INTERVAL_5);
        }
    };
    private final Runnable runnable10Min = new Runnable() {
        @Override
        public void run() {
            reportHeartbeat();
            handler.postDelayed(this, HEARTBEAT_INTERVAL_10);
        }
    };

    public void start() {
        handler.post(runnable5Min);
        handler.post(runnable10Min);
    }

    public void stop() {
        handler.removeCallbacks(runnable5Min);
        handler.removeCallbacks(runnable10Min);
    }

    private void reportOnline() {
        FodSDK.get().logEvent(FodConstants.Event.SCENE_ONLINE, getRole());
    }

    private void reportHeartbeat() {
        FodUser user = FodSDK.get().getUser();
        if (user == null) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", user.getUid());
        map.put("token", user.getToken());
        FodNet.post(new ApiCheckLogin(), map, new FodNet.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rsp = new JSONObject(response);
                    boolean status = rsp.optBoolean("status");
                    if (!status) {
                        ToastUtil.show(rsp.optString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    abstract FodRole getRole();
}

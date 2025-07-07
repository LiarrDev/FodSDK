package com.fodsdk.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.fodsdk.net.FodRepository;

public class FodPayResultPollingTask {
    private static final long FIRST_DELAY = 5000;           // 第一次请求延时 5 秒
    private static final long POLL_INTERVAL = 10 * 1000;    // 后续请求间隔 10 秒
    private static final long MAX_DURATION = 5 * 60 * 1000; // 最大持续时间 5 分钟
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long startTime;
    private final FodRepository repo;
    private final String order;
    private final FodCallback<Void> callback;

    public FodPayResultPollingTask(FodRepository repo, String order, FodCallback<Void> callback) {
        this.repo = repo;
        this.order = order;
        this.callback = callback;
    }

    public void startPolling() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderStatus();
                pollAgain();
            }
        }, FIRST_DELAY);
    }

    private void pollAgain() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderStatus();
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < MAX_DURATION) {
                    pollAgain();
                }
            }
        }, POLL_INTERVAL);
    }

    private void getOrderStatus() {
        repo.getOrderStatus(order, new FodCallback<Pair<Boolean, Boolean>>() {
            @Override
            public void onValue(Pair<Boolean, Boolean> pair) {
                boolean success = pair.first;
                boolean cancel = pair.second;
                if (success) {
                    callback.onValue(null);
                }
                if (cancel) {
                    stopPolling();
                }
            }
        });
    }

    public void stopPolling() {
        handler.removeCallbacksAndMessages(null);
    }
}

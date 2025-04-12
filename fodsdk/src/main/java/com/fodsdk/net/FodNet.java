package com.fodsdk.net;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fodsdk.net.api.FodBaseApi;
import com.fodsdk.utils.LogUtil;

import java.util.Map;

public class FodNet {

    private static RequestQueue queue;

    public static void init(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static void post(FodBaseApi api, Map<String, String> map, Callback callback) {
        String requestId = generateRequestId();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                api.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.v("POST[" + requestId + "] response: " + response);
                        LogUtil.v("================================");
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                LogUtil.v("================================");
                LogUtil.v("POST[" + requestId + "] request: " + api.getUrl());
                LogUtil.v("POST[" + requestId + "] params: " + map.toString());
                LogUtil.v("================================");
                return map;
            }
        };
        queue.add(request);
    }

    public static void get(FodBaseApi api, Map<String, String> map, Callback callback) {
        Uri.Builder builder = Uri.parse(api.getUrl()).buildUpon();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        String url = builder.toString();
        String requestId = generateRequestId();
        LogUtil.v("================================");
        LogUtil.e("GET[" + requestId + "] request: " + url);
        LogUtil.v("================================");
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.v("GET[" + requestId + "] response: " + response);
                        LogUtil.v("================================");
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );
        queue.add(request);
    }

    private static String generateRequestId() {
        String current = String.valueOf(System.currentTimeMillis());
        return current.substring(current.length() - 5);
    }

    public interface Callback {

        void onResponse(String response);

        default void onError(Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}

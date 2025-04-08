package com.fodsdk.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fodsdk.utils.LogUtil;

import java.util.Map;

public class FodNet {

    private static RequestQueue queue;

    public static void init(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static void post(FodBaseApi api, Map<String, String> map, Callback callback) {
        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                api.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                return map;
            }
        };
        queue.add(postRequest);
    }

    public interface Callback {

        void onResponse(String response);

        default void onError(Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}

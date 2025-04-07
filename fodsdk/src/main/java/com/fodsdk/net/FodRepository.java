package com.fodsdk.net;

import com.fodsdk.entities.FodGameConfig;
import com.fodsdk.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FodRepository {

    private final Gson gson = new Gson();

    public void init(FodGameConfig config) {
        try {
            String json = gson.toJson(config);
            JSONObject obj = new JSONObject(json);
            FodNet.post(new ApiInit(), obj, new FodNet.Callback() {
                @Override
                public void onResponse(JSONObject response) {
                    boolean status = response.optBoolean("status");
                    if (status) {
                        // TODO
                    } else {
                        ToastUtil.show(response.optString("data"));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

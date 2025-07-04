package com.fodsdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;

import com.fodsdk.core.FodCallback;
import com.fodsdk.core.FodConstants;
import com.fodsdk.net.response.DoPayResponse;
import com.fodsdk.ui.view.FodWebView;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;
import com.google.gson.Gson;

public class FodPayDialog extends FodBaseDialog {

    private final String payToken;
    private FodWebView webView;
    private ImageView ivClose;
    private FodCallback<String> callback;

    public FodPayDialog(Context context, String payToken, FodCallback<String> onPayCallback) {
        super(context);
        this.payToken = payToken;
        this.callback = onPayCallback;
    }

    @Override
    protected void initViews(View rootView) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        webView = rootView.findViewById(ResourceUtil.getViewId("web_view"));
        ivClose = rootView.findViewById(ResourceUtil.getViewId("iv_close"));
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Uri.Builder builder = Uri.parse(FodConstants.Inner.URL_PAYMENT).buildUpon();
        builder.appendQueryParameter("pay_token", payToken);
        String url = builder.toString();
        LogUtil.v("FodPayDialog url: " + url);
        webView.addJavascriptInterface(new PayInterface(), "Android");
        webView.loadUrl(url);
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_pay";
    }

    private class PayInterface {

        private final Gson gson = new Gson();

        @JavascriptInterface
        public void dismissDialog() {
            LogUtil.d("PayInterface dismissDialog");
            dismiss();
        }

        @JavascriptInterface
        public void doPay(String json) {
            LogUtil.d("PayInterface doPay: " + json);
            DoPayResponse response = gson.fromJson(json, DoPayResponse.class);
            if (response == null) {
                return;
            }
            String scheme = response.getScheme();
            if (scheme != null && isScheme(scheme)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
                    getContext().startActivity(intent);
                    callback.onValue(response.getOrder());
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            String url = response.getUrl();
            Activity activity = getOwnerActivity();
            if (url != null && activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(url);
                    }
                });
            }
        }

        private boolean isScheme(String url) {
            return url.startsWith(FodConstants.SCHEME.WECHAT) || url.startsWith(FodConstants.SCHEME.ALIPAY);
        }
    }
}

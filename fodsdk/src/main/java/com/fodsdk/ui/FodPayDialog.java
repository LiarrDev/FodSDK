package com.fodsdk.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.fodsdk.core.FodConstants;
import com.fodsdk.ui.view.FodWebView;
import com.fodsdk.utils.LogUtil;
import com.fodsdk.utils.ResourceUtil;

public class FodPayDialog extends FodBaseDialog {

    private final String payToken;

    public FodPayDialog(Context context, String payToken) {
        super(context);
        this.payToken = payToken;
    }

    @Override
    protected void initViews(View rootView) {
        FodWebView webView = rootView.findViewById(ResourceUtil.getViewId("web_view"));

        Uri.Builder builder = Uri.parse(FodConstants.FOD_PAYMENT).buildUpon();
        builder.appendQueryParameter("pay_token", payToken);
        String url = builder.toString();
        LogUtil.v("FodPayDialog url: " + url);
        webView.loadUrl(url);
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_pay";
    }
}

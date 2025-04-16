package com.fodsdk.ui.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fodsdk.core.FodConstants;

public class FodWebView extends WebView {
    public FodWebView(Context context) {
        super(context);
        init();
    }

    public FodWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FodWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FodWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (scheme(uri.toString())) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private boolean scheme(String url) {
        return url.startsWith(FodConstants.SCHEME.WECHAT) || url.startsWith(FodConstants.SCHEME.ALIPAY);
    }
}

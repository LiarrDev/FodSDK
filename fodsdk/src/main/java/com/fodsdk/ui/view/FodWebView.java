package com.fodsdk.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
    }
}

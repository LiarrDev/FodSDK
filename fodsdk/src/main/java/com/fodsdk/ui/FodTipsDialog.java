package com.fodsdk.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fodsdk.utils.ResourceUtil;

public class FodTipsDialog extends FodBaseDialog {

    private final String message;
    private TextView tvMessage;

    public FodTipsDialog(Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void initViews(View rootView) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        tvMessage = rootView.findViewById(ResourceUtil.getViewId("tv_message"));
        tvMessage.setText(message);
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_tips";
    }

    @Override
    public void show() {
        super.show();
        tvMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1500);
    }
}

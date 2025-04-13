package com.fodsdk.ui;

import android.content.Context;
import android.view.View;

import com.fodsdk.core.FodCallback;
import com.fodsdk.utils.ResourceUtil;

public class FodExitDialog extends FodBaseDialog {

    private final FodCallback<Boolean> callback;

    public FodExitDialog(Context context, FodCallback<Boolean> callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    protected void initViews(View rootView) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        rootView.findViewById(ResourceUtil.getViewId("btn_confirm")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onValue(true);
                dismiss();
            }
        });
        rootView.findViewById(ResourceUtil.getViewId("btn_cancel")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onValue(false);
                dismiss();
            }
        });
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_exit";
    }
}

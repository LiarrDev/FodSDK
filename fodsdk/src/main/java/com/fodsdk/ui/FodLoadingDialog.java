package com.fodsdk.ui;

import android.content.Context;
import android.view.View;

public class FodLoadingDialog extends FodBaseDialog {
    public FodLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void initViews(View rootView) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_loading";
    }
}

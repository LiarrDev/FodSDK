package com.fodsdk.ui;

import android.app.AlertDialog;
import android.content.Context;

import com.fodsdk.settings.GlobalSettings;

public class FodAgreementDialog extends AlertDialog {

    public FodAgreementDialog(Context context) {
        super(context);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        GlobalSettings.setFirstLaunch(false);
    }
}

package com.aamaldonado.viaje.seguro.utpl.tft.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class AlertComponents {

    @SuppressLint("UseCompatLoadingForDrawables")
    public MaterialAlertDialogBuilder alertDialogMD(Activity activity, String title, String description) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setBackground(activity.getResources().getDrawable(R.drawable.alert_dialog_bg, null));
        return builder;
    }

    public void setSnakbar(View view, String description) {
        Snackbar snackbar = Snackbar.make(view, description, BaseTransientBottomBar.LENGTH_LONG).setDuration(4000);
        snackbar.show();
    }
}

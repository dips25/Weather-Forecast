package com.example.weatherforecast;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class CustomAlertDialog extends AlertDialog.Builder {

    public static CustomAlertDialog builder;

    public CustomAlertDialog(@NonNull Context context) {
        super(context);
    }




    public AlertDialog.Builder createAlertDialog(String title) {

        this.setTitle("Error")
                .setIcon(R.drawable.warning)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setMessage(title)
                .setCancelable(false)
                .create();
        return this;

    }
}

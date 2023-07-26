package com.example.weatherforecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;



public class InternetBoadcastReceiver extends BroadcastReceiver {

    boolean isConnected;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {

            Toast.makeText(context, "Connectivity Changed", Toast.LENGTH_SHORT).show();

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            for (NetworkInfo n : networkInfo) {

                if (n.isConnectedOrConnecting()) {

                    isConnected=true;
                    break;

                } else {

                    isConnected = false;
                }

                }

            if (!isConnected) {

                CustomAlertDialog customAlertDialog = new CustomAlertDialog(context);
                customAlertDialog.createAlertDialog("No Connectivity");
                customAlertDialog.show();
                customAlertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SettingsScreen settingsScreen = SettingsScreen.getInstance();
                        settingsScreen.showNetworkSettingScreen(context);
                        dialog.dismiss();



                    }
                }).setCancelable(false);

                customAlertDialog.create();
                customAlertDialog.show();

            }






            }
        }


}

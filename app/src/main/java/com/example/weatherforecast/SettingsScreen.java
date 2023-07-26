package com.example.weatherforecast;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class SettingsScreen {


    public static SettingsScreen settingsScreen;


    public static SettingsScreen getInstance() {

        if (settingsScreen == null) {

            return new SettingsScreen();
        }

        return settingsScreen;



    }




        public void _showSettingScreen(Context context , String intentStr)
        {
            try
            {
                Intent intent = new Intent(intentStr);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }

        public void showSettingScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.SETTINGS");
        }

        public  void showAPNScreen(Context context)
        {
            _showSettingScreen(context , "android.settings.APN_SETTINGS");
        }

        public void showLocationScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.LOCATION_SOURCE_SETTINGS");
        }

        public  void showSecurityScreen(Context context)
        {
            _showSettingScreen(context , "android.settings.SECURITY_SETTINGS");
        }

        public  void showWifiScreen(Context context)
        {
            _showSettingScreen(context , "android.settings.WIFI_SETTINGS");
        }

        public  void showBluetoothScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.BLUETOOTH_SETTINGS");
        }

        public  void showDateScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.DATE_SETTINGS");
        }

        public  void showSoundScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.SOUND_SETTINGS");
        }

        public  void showDisplayScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.DISPLAY_SETTINGS");
        }

        public  void showApplicationScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.APPLICATION_SETTINGS");
        }

        public  void showNetworkSettingScreen(Context context)
        {
            showDataRoamingScreen(context);
        }

        public void showNetworkOperatorScreen(Context context)
        {
            if(Build.VERSION.SDK_INT > 15)
            {
                _showSettingScreen(context,  "android.settings.NETWORK_OPERATOR_SETTINGS");
            }
            else
            {
                Intent intent=new Intent(Settings.ACTION_SETTINGS);
                intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

        public void showDataRoamingScreen(Context context)
        {
            if(Build.VERSION.SDK_INT > 15)
            {
                _showSettingScreen(context , "android.settings.DATA_ROAMING_SETTINGS");
            }
            else
            {
                Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
                intent.setComponent(cName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

        public  void showDataMobileScreen(Context context)
        {
            if(Build.VERSION.SDK_INT > 15)
            {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);//android.provider.Settings.ACTION_SETTINGS //Intent.ACTION_MAIN
                intent.setClassName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else
            {
                showDataRoamingScreen(context);
            }
        }

        public  void showNotificationScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.NOTIFICATION_SETTINGS");
        }

        public void showBatterySaverScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.BATTERY_SAVER_SETTINGS");
        }

        public  void showNfcScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.NFC_SETTINGS");
        }

        public  void showInternalStorageScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.INTERNAL_STORAGE_SETTINGS");
        }

        public  void showDictionarySettingScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.USER_DICTIONARY_SETTINGS");
        }

        public  void showManageApplicationsScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.MANAGE_APPLICATIONS_SETTINGS");
        }

        public  void showManageAllApplicationsScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS");
        }

        public void showMemoryCardScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.MEMORY_CARD_SETTINGS");
        }



        public void showWirelessScreen(Context context)
        {
            _showSettingScreen(context,"android.settings.WIRELESS_SETTINGS");
        }

        public static void showWifiScreenSafe(Context context)
        {
            try
            {
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            catch (Exception e)
            {}
        }
    }


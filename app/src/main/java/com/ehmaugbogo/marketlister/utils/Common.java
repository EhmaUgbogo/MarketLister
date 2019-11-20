package com.ehmaugbogo.marketlister.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.pixplicity.easyprefs.library.Prefs;

public class Common extends Application {
    public static final String NOTIFICATION_CHANNEL_ID="com.ehmaugbogo.marketlister_NOTIFICATION_CHANNEL_ID";
    private static ListItemHolder currentHolder;

    @Override
    public void onCreate() {
        super.onCreate();

        iniPrefLib();
        createNoticationChannel();

    }

    private void createNoticationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"MarketList Channel1", NotificationManager.IMPORTANCE_HIGH);
            channel.shouldVibrate();
            channel.setDescription("Notify users on important updates with vibration and sound");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void iniPrefLib() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static ListItemHolder getCurrentHolder() {
        return currentHolder;
    }

    public static void setCurrentHolder(ListItemHolder currentHolder) {
        Common.currentHolder = currentHolder;
    }
}



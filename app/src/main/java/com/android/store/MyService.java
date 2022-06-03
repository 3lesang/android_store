package com.android.store;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import static  com.android.store.MyApplication.CHANNEL_ID;


public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Tinconder","myservice");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String string= intent.getStringExtra("key");
        sendNotification(string);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tinconder","myservice");
    }

    public void sendNotification(String string)
    {
        Intent intent = new Intent(this, Admin.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("APP STORE MOBILE")
                .setContentText(string)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

    }
}

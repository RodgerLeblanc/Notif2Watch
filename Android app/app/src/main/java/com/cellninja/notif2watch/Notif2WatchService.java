package com.cellninja.notif2watch;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Vector;

/**
 * Created by Roger on 16-10-02.
 */

public class Notif2WatchService extends Service {
    final Context context = this;
    private BroadcastReceiver messageReceiver;
    private BroadcastReceiver batteryReceiver;
    private Integer lastBatteryLevel = -1;
    private Integer someNumber = 0;
    private VectorStream vs = new VectorStream();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getAction().equals(Notif2WatchConstants.ACTION.STARTFOREGROUND_ACTION)) {
                Log.i(Notif2WatchConstants.DEFAULT_VALUES.LOG_TAG, "Received Start Foreground Intent ");
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setAction(Notif2WatchConstants.ACTION.MAIN_ACTION);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("Running in background")
                        .setTicker("Connected to Pebble app")
                        .setContentText("Must use a Notif2Watch watchface.")
                        .setSmallIcon(R.mipmap.notif2watch_status_bar)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .build();
                startForeground(Notif2WatchConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            }

            PebbleNotification.registerNotif2WatchUuids(this);
            PebbleNotification.registerReceivedAckAndNack(this);

            // Send battery info on startup
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = (int)((level*100) / (float)scale);
            if (percent >= 0) PebbleNotification.sendBatteryToWatchface(context, percent);
        }
        catch (Exception e) {
            Log.i(this.getClass().getName(), "Exception catched: " + e.getMessage());
        }

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Notif2WatchConstants.DEFAULT_VALUES.MESSAGE_PASSING_INTENT_ACTION);
        theFilter.addAction(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_CHANGED);
//        theFilter.addAction(Notif2WatchConstants.DEFAULT_VALUES.BATTERY_CHANGED);
        this.messageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("Notif2WatchService", intent.getAction() + " -- " + intent.toString());

                /*
                // Debug logging
                String message = "Notif2WatchService - " + intent.getAction() + " -- " + intent.toString();
                Intent i1 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                i1.putExtra("notification_event", message);
                sendBroadcast(i1);
                */

                switch (intent.getAction()) {
                    case Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_CHANGED: {
                        PebbleNotification.sendToWatchface(context, intent.getIntArrayExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES));

                        SharedPreferences sharedPref = context.getSharedPreferences(Notif2WatchConstants.DEFAULT_VALUES.PREFS_FILE_NAME, Context.MODE_PRIVATE);
                        String defaultValue = "";
                        String n2wKey = sharedPref.getString(Notif2WatchConstants.SHARED_PREFS_KEYS.N2W_KEY, defaultValue); // 1483848884889

                        if (!n2wKey.isEmpty())
                           vs.sendToVectorStream(context, intent.getIntArrayExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES), n2wKey);
                        break;
                    }
                    case Notif2WatchConstants.DEFAULT_VALUES.BATTERY_CHANGED: {
                        int level = intent.getIntExtra(Notif2WatchConstants.EXTRAS.BATTERY_LEVEL, -1);
                        if (level >= 0) {
                            PebbleNotification.sendBatteryToWatchface(context, level);
                            lastBatteryLevel = level;
                        }
                        break;
                    }
                    default: {
                        PebbleNotification.sendToWatchface(context, intent.getIntArrayExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES));
                        break;
                    }
                }
            }
        };
        this.registerReceiver(this.messageReceiver, theFilter);

        this.batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int percent = (int)((level*100) / (float)scale);
                if ((percent >= 0) && (percent != lastBatteryLevel)) {
                    PebbleNotification.sendBatteryToWatchface(context, percent);
                    lastBatteryLevel = percent;

                    Intent i = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                    i.putExtra("notification_event","Phone battery: " + String.valueOf(percent) + "% (" + String.valueOf(level) + "/" + String.valueOf(scale) + ")");
                    sendBroadcast(i);
                }
            }
        };
        this.registerReceiver(this.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do not forget to unregister the receiver!!!
        this.unregisterReceiver(this.messageReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

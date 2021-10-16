package com.cellninja.notif2watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cellninja.notif2watch.Notif2WatchConstants;

/*
Automatically start Service on phone boot
 */
public class StartUpBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(Notif2WatchConstants.DEFAULT_VALUES.LOG_TAG, "StartUpBootReceiver BOOT_COMPLETED");
            Intent startIntent = new Intent(context, Notif2WatchService.class);
            startIntent.setAction(Notif2WatchConstants.ACTION.STARTFOREGROUND_ACTION);
            context.startService(startIntent);
        }
    }
}
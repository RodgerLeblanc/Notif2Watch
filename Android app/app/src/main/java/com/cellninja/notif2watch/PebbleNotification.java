package com.cellninja.notif2watch;

import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Roger on 16-10-02.
 */

public class PebbleNotification {
    public static void sendAlertToPebble(Context context, String title, String body) {
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

        final Map data = new HashMap();
        data.put("title", title);
        data.put("body", body);
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();

        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "MyAndroidApp");
        i.putExtra("notificationData", notificationData);

        context.sendBroadcast(i);
    }

    public static void sendAlertToPebble(Context context, JSONObject jsonData) {
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");
        final String notificationData = new JSONArray().put(jsonData).toString();

        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "MyAndroidApp");
        i.putExtra("notificationData", notificationData);

        context.sendBroadcast(i);
    }

    public static void registerNotif2WatchUuids(Context context) {
        String baseUuid = "28AF3DC7-E40D-490F-BEF2-29548C8BEEA";
        String[] notifUuid = {"1"};
        for (Integer i = 0; i < notifUuid.length; i++) {
            UUID thisUuid = UUID.fromString(baseUuid + notifUuid[i]);
            AppMessageReceiver appMessageReceiver = new AppMessageReceiver(context);
            appMessageReceiver.thisUuid = thisUuid;
            appMessageReceiver.port = Notif2WatchConstants.DEFAULT_VALUES.H2W_SENDING_PORT;
            new Thread(appMessageReceiver).start();
        }

        /*
        AppMessageReceiver bridgeWAappMessageReceiver = new AppMessageReceiver(context);
        bridgeWAappMessageReceiver.thisUuid = UUID.fromString("6B6968BB-7B1A-431A-B8CD-D55C5A4F281F");
        bridgeWAappMessageReceiver.port = Notif2WatchConstants.DEFAULT_VALUES.BRIDGE_SENDING_PORT;
        new Thread(bridgeWAappMessageReceiver).start();

        AppMessageReceiver bridgeTALKappMessageReceiver = new AppMessageReceiver(context);
        bridgeTALKappMessageReceiver.thisUuid = UUID.fromString("E466C32E-7FD6-4849-99C5-A988E8ED84A7");
        bridgeTALKappMessageReceiver.port = Notif2WatchConstants.DEFAULT_VALUES.BRIDGE_SENDING_PORT;
        new Thread(bridgeTALKappMessageReceiver).start();
        */
    }

    public static void registerReceivedAckAndNack(Context context) {
        ReceivedAckAndNack notif2watchAckAndNackReceiver = new ReceivedAckAndNack(context);
        notif2watchAckAndNackReceiver.thisUuid = UUID.fromString(Notif2WatchConstants.DEFAULT_VALUES.N2W_UUID);
        notif2watchAckAndNackReceiver.port = Notif2WatchConstants.DEFAULT_VALUES.BRIDGE_SENDING_PORT;
        new Thread(notif2watchAckAndNackReceiver).start();
    }

    public static void sendToWatchface(Context context, int[] tupleValues) {
        if (tupleValues.equals(null)) { return; }
        // Create a new dictionary
        PebbleDictionary dict = new PebbleDictionary();
        int i=0;
        for (int tupleValue : tupleValues) {
            // Add data to the dictionary
            //dict.addString(AppKeyContactName, contactName);
            if ((tupleValue >= 0) && (i < 8)) {
                dict.addInt32(i, tupleValue);
                i++;
            }
        }

        int emptyIcon = 31;
        for (int j = i; j < 8; j++) {
            dict.addInt32(j, emptyIcon);
        }

        int slotCounterKey = 15;
        dict.addInt32(slotCounterKey, i);

        final UUID appUuid = UUID.fromString(Notif2WatchConstants.DEFAULT_VALUES.N2W_UUID);
        // Send the dictionary
        PebbleKit.sendDataToPebble(context, appUuid, dict);
        Log.i("sendToWatchface", "dict: " + dict.toJsonString());
    }

    public static void sendBatteryToWatchface(Context context, int level) {
        // Create a new dictionary
        PebbleDictionary dict = new PebbleDictionary();
        dict.addInt32(Notif2WatchConstants.WATCHFACE_MESSAGE_KEYS.PHONE_PERCENT, level);

        final UUID appUuid = UUID.fromString(Notif2WatchConstants.DEFAULT_VALUES.N2W_UUID);
        // Send the dictionary
        PebbleKit.sendDataToPebble(context, appUuid, dict);
        Log.i("sendToWatchface", "dict: " + dict.toJsonString());
    }
}

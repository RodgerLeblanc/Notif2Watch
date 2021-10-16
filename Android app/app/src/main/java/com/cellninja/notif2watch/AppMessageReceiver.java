package com.cellninja.notif2watch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Roger on 2015-10-25.
 */
public class AppMessageReceiver implements Runnable {
    private Context context;
    private PebbleKit.PebbleDataReceiver appMessageReceiver;
    public UUID thisUuid;
    public Integer port;
    private Integer lastTransactionId = -1;

    public AppMessageReceiver(Context context){
        this.context = context;
    }

    public void run() {
        if(appMessageReceiver == null) {
            appMessageReceiver = new PebbleKit.PebbleDataReceiver(thisUuid) {
                @Override
                public void receiveData(Context context, int transactionId, PebbleDictionary data) {
                    // Always ACK
                    PebbleKit.sendAckToPebble(context, transactionId);

                    if (transactionId == lastTransactionId) { return; }
                    lastTransactionId = transactionId;

                    // If we've been asked for a refresh
                    Long dumbValue = data.getInteger(Notif2WatchConstants.WATCHFACE_MESSAGE_KEYS.ASK_NOTIF2WATCH_REFRESH);
                    String message = "Received data for transaction " + transactionId + " " + data.toJsonString() + " -- " + context.toString();
                    if(dumbValue != null) {
                        Log.i("AppMessageReceiver", message);
                        Intent i = new Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_SERVICE);
                        i.putExtra("command", Notif2WatchConstants.EXTRAS.COMMAND_REFRESH_WATCHFACE);
                        context.sendBroadcast(i);

                        // Debug logging
                        Intent i1 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                        i1.putExtra("notification_event", message);
                        context.sendBroadcast(i1);
                    }
                }
            };

            // Add AppMessage capabilities
            PebbleKit.registerReceivedDataHandler(context, appMessageReceiver);
        }
    }
}
//Started with a "new Thread(threadA).start()" call

package com.cellninja.notif2watch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by Roger on 2015-11-17.
 */
public class ReceivedAckAndNack implements Runnable {
    static private Context context;
    static public UUID thisUuid;
    public Integer port;
    private PebbleKit.PebbleAckReceiver pebbleAckReceiver;
    private PebbleKit.PebbleNackReceiver pebbleNackReceiver;
    static private List<PebbleDictionary> pendingOutgoingDictionary;
 //   private int numberOfRetry = 0;

    public ReceivedAckAndNack(Context context){
        this.context = context;
    }

    static public void AddOutgoingDictionary(PebbleDictionary outgoing) {
        pendingOutgoingDictionary.add(outgoing);
        if (pendingOutgoingDictionary.size() == 1) {
            PebbleKit.sendDataToPebble(context, thisUuid, pendingOutgoingDictionary.get(0));
        }
    }

    public void run() {
        if(pebbleAckReceiver == null) {
            pebbleAckReceiver = new PebbleKit.PebbleAckReceiver(thisUuid) {
                @Override
                public void receiveAck(Context context, int transactionId) {
                    Log.i("ReceivedAckAndNack", "Received ack for transaction " + transactionId);
//                    PebbleNotification.sendAlertToPebble(context, "Received ack for transaction", Integer.toString(transactionId));

                    if ((transactionId < 100) || (transactionId == 255))
                        return;

                    try {
                        final JSONObject jObject = new JSONObject();
                        jObject.put("type", "ACK");
                        jObject.put("uuid", thisUuid.toString());
                        jObject.put("transactionId", Integer.toString(transactionId));
                        //Client.sendUdpMessage(jObject.toString(), port);
                    }
                    catch (Exception e) {
                        final Intent i = new Intent(Notif2WatchConstants.DEFAULT_VALUES.MESSAGE_PASSING_INTENT_ACTION);
                        i.putExtra("message", "Exception trying to send ack from Pebble to Notif2Watch: " + e.getMessage());
                        context.sendBroadcast(i);
//                        sendAlertToPebble("Exception trying to send data from Pebble to Bridge", e.getMessage());
                    }

                    // Remove the last message sent which was acked
//                    if (!pendingOutgoingDictionary.isEmpty())
//                        pendingOutgoingDictionary.remove(0);
//                    if (!pendingOutgoingDictionary.isEmpty())
//                        PebbleKit.sendDataToPebble(context, thisUuid, pendingOutgoingDictionary.get(0));
                }
            };
            PebbleKit.registerReceivedAckHandler(context, pebbleAckReceiver);
        }

        if(pebbleNackReceiver == null) {
            pebbleNackReceiver = new PebbleKit.PebbleNackReceiver(thisUuid) {
                @Override
                public void receiveNack(Context context, int transactionId) {
                    Log.i("ReceivedAckAndNack", "Received nack for transaction " + transactionId);
//                    PebbleNotification.sendAlertToPebble(context, "Received nack for transaction", Integer.toString(transactionId));

                    if ((transactionId < 100) || (transactionId == 255))
                        return;

                    try {
                        final JSONObject jObject = new JSONObject();
                        jObject.put("type", "NACK");
                        jObject.put("uuid", thisUuid.toString());
                        jObject.put("transactionId", Integer.toString(transactionId));
                        //Client.sendUdpMessage(jObject.toString(), port);
                    }
                    catch (Exception e) {
                        final Intent i = new Intent(Notif2WatchConstants.DEFAULT_VALUES.MESSAGE_PASSING_INTENT_ACTION);
                        i.putExtra("message", "Exception trying to send nack from Pebble to Notif2Watch: " + e.getMessage());
                        context.sendBroadcast(i);
//                        sendAlertToPebble("Exception trying to send data from Pebble to Notif2Watch", e.getMessage());
                    }

                    /*
                    numberOfRetry++;
                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    if (numberOfRetry < 10) {
                        if (!pendingOutgoingDictionary.isEmpty())
                            PebbleKit.sendDataToPebble(context, thisUuid, pendingOutgoingDictionary.get(0));
                    }
                    else {
                        numberOfRetry = 0;
                        if (!pendingOutgoingDictionary.isEmpty())
                            pendingOutgoingDictionary.remove(0);
                        if (!pendingOutgoingDictionary.isEmpty())
                            PebbleKit.sendDataToPebble(context, thisUuid, pendingOutgoingDictionary.get(0));
                    }
                    */
                }
            };
            PebbleKit.registerReceivedNackHandler(context, pebbleNackReceiver);
        }
    }
}
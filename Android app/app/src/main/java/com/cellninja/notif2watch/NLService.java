package com.cellninja.notif2watch;

/**
 * Created by Roger on 16-09-30.
 */

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereceiver;
    private JSONObject packageNameFromGitHub = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"************** onCreate()");

        nlservicereceiver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_SERVICE);
        registerReceiver(nlservicereceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (packageNameFromGitHub == null) {
            this.initializePackageNameFromGithub();
        }

        if (isUnwantedPackage(sbn.getPackageName())) { return; }
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Bundle extras = NotificationCompat.getExtras(sbn.getNotification());
        String title = extras.getString("android.title");
        String text = extras.getString("android.text");

        Intent i = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
        i.putExtra("notification_event","Posted: " + sbn.getPackageName() + " -- " + sbn.getNotification().tickerText + " -- " + NotificationCompat.getCategory(sbn.getNotification()) + " -- " + title + " -- " + text + "\n");
        //i.putExtra("notification_icon","onNotificationPosted :" + sbn.getNotification().getSmallIcon().toString() + "\n");
        sendBroadcast(i);

        Intent i2 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_CHANGED);
        i2.putExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES, getTupleValues());
        sendBroadcast(i2);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (isUnwantedPackage(sbn.getPackageName())) { return; }
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());
        Intent i = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
        i.putExtra("notification_event","Removed: " + sbn.getPackageName() + "\n");
        sendBroadcast(i);

        Intent i2 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_CHANGED);
        i2.putExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES, getTupleValues());
        sendBroadcast(i2);
    }

    private Boolean isUnwantedPackage(String packageName) {
        String[] unwantedPackages = {"com.android.systemui", "com.google.android.googlequicksearchbox"};
        for (Integer i = 0; i < unwantedPackages.length; i++) {
            if (unwantedPackages[i].equals(packageName))
                return true;
        }
        return false;
    }

    private int[] getTupleValues() {
        if (packageNameFromGitHub != null) { Log.i("JSON???", "************** " + packageNameFromGitHub.toString()); }
        List<Integer> values = new ArrayList<Integer>();
        List<String> groups = new ArrayList<String>();
        int i = 0;
        for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
            String group = NotificationCompat.getGroup(sbn.getNotification());
            if (groups.contains(group)) { continue; }
            if ((group != null) && (!group.isEmpty())) { groups.add(group); }
            int tupleValue = translatePackageToTupleValue(sbn);
            if ((tupleValue < 0) && (packageNameFromGitHub != null)) {
                if (packageNameFromGitHub.containsKey(sbn.getPackageName())) {
                    tupleValue = ((Long)packageNameFromGitHub.get(sbn.getPackageName())).intValue();
                }
            }
            values.add(tupleValue);
            i++;
        }
        int[] tupleValues = new int[values.size()];
        for (int iter = 0; iter < values.size(); iter++) { tupleValues[iter] = values.get(iter); }
        return tupleValues;
    }

    private Integer translatePackageToTupleValue(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        String category = (Build.VERSION.SDK_INT >= 21) ? sbn.getNotification().category : NotificationCompat.getCategory(sbn.getNotification());
        switch (packageName) {
            case "com.cellninja.notif2watch": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_NOTIF2WATCH_34.ordinal();
            }
            case "com.calea.echo":
            case "com.android.messaging":
            case "com.textra":
            case "com.sonyericsson.conversations":
            case "com.android.mms":
            case "com.google.android.apps.messaging":
            case "com.samsung.android.messaging": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TEXT_34.ordinal();
            }
            case "com.microsoft.office.outlook": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_EMAIL_34.ordinal();
            }
            case "com.readdle.spark": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SPARK_34.ordinal();
            }
            case "com.bbm": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_BBM_34.ordinal();
            }
            case "com.facebook.katana": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_FACEBOOK_34.ordinal();
            }
            case "com.google.android.apps.inbox":
            case "com.google.android.gm": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GMAIL_34.ordinal();
            }
            case "com.facebook.orca": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_MESSENGER_34.ordinal();
            }
            case "com.Slack": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SLACK_34.ordinal();
            }
            case "com.joelapenna.foursquared": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_FOURSQUARE_34.ordinal();
            }
            case "com.instagram.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_INSTAGRAM_34.ordinal();
            }
            case "com.just10.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_JUST10_34.ordinal();
            }
            case "com.linkedin.android.jobs.jobseeker":
            case "com.linkedin.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_LINKEDIN_34.ordinal();
            }
            case "com.google.android.talk":
            case "com.google.android.apps.hangoutsdialer": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_TALK_34.ordinal();
            }
            case "com.whatsapp": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_WHATSAPP_34.ordinal();
            }
            case "com.vkontakte.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_VK10_34.ordinal();
            }
            case "com.tencent.mm": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_WECHAT_34.ordinal();
            }
            case "com.google.android.music":
            case "com.google.android.apps.playconsole":
            case "com.android.vending": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_PLAY_34.ordinal();
            }
            case "com.android.phone": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CALL_34.ordinal();
            }
            case "com.skype.raider": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SKYPE_34.ordinal();
            }
            case "com.blackberry.hub": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_HUB_34.ordinal();
            }
            case "com.twitter.android":
            case "com.hootsuite.droid.full":
            case "twitter.android.finch": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TWITTER_34.ordinal();
            }
            case "com.samsung.android.app.scrollcapture": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SCREENSHOT_34.ordinal();
            }
            case "com.google.android.youtube": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_YOUTUBE_34.ordinal();
            }
            case "org.telegram.messenger": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TELEGRAM_34.ordinal();
            }
            case "com.pushbullet.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_PUSHBULLET_34.ordinal();
            }
            case "com.discord": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_DISCORD_34.ordinal();
            }
            case "jp.naver.line.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_LINE_34.ordinal();
            }
            case "com.facebook.appmanager": {
                Bundle extras = NotificationCompat.getExtras(sbn.getNotification());
                String title = extras.getString("android.title");
                switch (title) {
                    case "Instagram": return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_INSTAGRAM_34.ordinal();
                    case "Facebook":
                    default: return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_FACEBOOK_34.ordinal();
                }
            }
            case "com.badoo.mobile": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_BADOO_34.ordinal();
            }
            case "com.box.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_BOX_34.ordinal();
            }
            case "com.android.chrome": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CHROME_DOWNLOAD_34.ordinal();
            }
            case "com.dropbox.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_DROPBOX_34.ordinal();
            }
            case "com.google.android.apps.docs": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_DRIVE_34.ordinal();
            }
            case "com.fiverr.fiverr": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_FIVERR_34.ordinal();
            }
            case "com.google.android.apps.plus": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_PLUS_34.ordinal();
            }
            case "com.meetup": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_MEETUP_34.ordinal();
            }
            case "com.microsoft.skydrive": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_ONEDRIVE_34.ordinal();
            }
            case "com.paypal.android.p2pmobile": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_PAYPAL_34.ordinal();
            }
            case "tv.periscope.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_PERISCOPE_34.ordinal();
            }
            case "com.pinterest": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_PINTEREST_34.ordinal();
            }
            case "com.snapchat.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SNAPCHAT_34.ordinal();
            }
            case "com.tinder": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TINDER_34.ordinal();
            }
            case "com.tumblr": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TUMBLR_34.ordinal();
            }
            case "com.viber.voip": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_VIBER_34.ordinal();
            }
            case "co.vine.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_VINE_34.ordinal();
            }
            case "com.grindrapp.android": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GRINDR_34.ordinal();
            }
            case "com.google.android.apps.photos": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_PHOTOS_34.ordinal();
            }
            case "de.orrs.deliveries": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_DELIVERIES_34.ordinal();
            }
            case "com.android.server.telecom": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CALL_34.ordinal();
            }
            case "com.android.providers.downloads": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CHROME_DOWNLOAD_34.ordinal();
            }
            case "android": {
                if (category == "status")
                    return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TETHERING_34.ordinal();
                else
                    break;
            }
            case "com.google.android.keep": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_KEEP_34.ordinal();
            }
            case "com.eurosport": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_EUROSPORT_34.ordinal();
            }
            case "com.lemonde.androidapp": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_LE_MONDE_34.ordinal();
            }
            case "ch.threema.app": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_THREEMA_34.ordinal();
            }
            case "de.sde.mobile": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SZDE_34.ordinal();
            }
            case "de.hafas.android.db": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_DB_NAVIGATOR_34.ordinal();
            }
            case "com.runtastic.android":
            case "com.runtastic.android.pro2":
            case "com.nike.plusgps":
            case "com.mapmyrun.android2":
            case "com.adidas.micoach":
            case "com.fitness22.running":
            case "com.grinasys.weightlossgoogleplay":
            case "com.sixtostart.zombiesrunclient":
            case "com.strava":
            case "com.endomondo.android":
            case "com.sec.android.app.shealth":
            case "com.fitnesskeeper.runkeeper.pro": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_RUNNER_34.ordinal();
            }
            case "com.sec.android.app.music": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_MUSIC_34.ordinal();
            }
            case "com.google.android.apps.fireball": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_GOOGLE_ALLO_34.ordinal();
            }
            case "com.wordpress.ninedof.dashboard": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_DASHBOARD_34.ordinal();
            }
            case "org.thoughtcrime.securesms": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_SIGNAL_34.ordinal();
            }
            case "com.microsoft.teams": {
                return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TEAMS_34.ordinal();
            }
            default: {
                break;
            }
        }

        if (category == Notification.CATEGORY_EMAIL) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_EMAIL_34.ordinal();
        }
        else if ((category == Notification.CATEGORY_MESSAGE) || (category == "msg")) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TEXT_34.ordinal();
        }
        else if (category == Notification.CATEGORY_SYSTEM) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_ANDROID_34.ordinal();
        }
        else if (category == Notification.CATEGORY_EVENT) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CALENDAR_34.ordinal();
        }
        else if (category == Notification.CATEGORY_CALL) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CALL_34.ordinal();
        }
        if (category == "Missed call") {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CALL_34.ordinal();
        }
        if (category == Notification.CATEGORY_ALARM) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_NOTIFICATION_34.ordinal();
        }

        if (packageName.toLowerCase().contains("tweetcaster")) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_TWITTER_34.ordinal();
        }
        else if (packageName.toLowerCase().contains("calendar")) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_CALENDAR_34.ordinal();
        }
        else if (packageName.toLowerCase().contains("email")) {
            return Notif2WatchConstants.Icons.RESOURCE_ID_ICON_EMAIL_34.ordinal();
        }

        return -1;
    }

    private void initializePackageNameFromGithub() {
        Log.i("JSON???", "************** calling getStream()");
        JSONObject object = getStream("http://rodgerleblanc.github.io/Notif2Watch/packageNameToIcon.json");
        if (object != null) {
            packageNameFromGitHub = object;

            Intent i = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
            i.putExtra("notification_event","===== packageNameFromGitHub ===="
                    + "\n" + packageNameFromGitHub.toJSONString()
                    + "\n" + "===== packageNameFromGitHub ====");
            sendBroadcast(i);
        }
    }

    private JSONObject getStream(String urlToQuery) {
        try {
            Log.i("JSON???", "************** ");
            URL url = new URL(urlToQuery);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(10000);
            InputStream inputStream = urlConnection.getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
            Log.i("JSON???", "************** " + jsonObject.toString());
            return jsonObject;
        } catch (Exception ex) {
            Log.i("JSON???", "************** Exception: " + ex.getMessage());
            return null;
        }
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"********** onReceive()");
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                try {
                    initializePackageNameFromGithub();
                    Intent i1 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                    i1.putExtra("notification_event","=====================");
                    sendBroadcast(i1);
                    int i=1;
                    for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                        Bundle extras = NotificationCompat.getExtras(sbn.getNotification());
                        String title = extras.getString("android.title");
                        String text = extras.getString("android.text");

                        Intent i2 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                        i2.putExtra("notification_event",i +" " + sbn.getPackageName()
                                + ((NotificationCompat.getActionCount(sbn.getNotification()) > 0) ? "\nAction: " + NotificationCompat.getAction(sbn.getNotification(), 0).toString() : "")
                                + "\n" + title
                                + "\n" + text
                                + "\nCategory: " + NotificationCompat.getCategory(sbn.getNotification())
                                + "\nGroup: " + NotificationCompat.getGroup(sbn.getNotification())
                                + "\nExtras: " + NotificationCompat.getExtras(sbn.getNotification()).toString() + "\n");
                        sendBroadcast(i2);
                        i++;
                    }
                    Intent i3 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                    i3.putExtra("notification_event","===== Notification List ====");
                    sendBroadcast(i3);

                    Intent i2 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_CHANGED);
                    i2.putExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES, getTupleValues());
                    sendBroadcast(i2);

                    initializePackageNameFromGithub();
                }
                catch (Exception e) {
                    Log.i("NLServiceReceiver", "************** Exception: " + e.getMessage());
                    // Debug logging
                    String message = "NLServiceReceiver - OnReceive exception - " + e.getMessage();
                    Intent i1 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                    i1.putExtra("notification_event", message);
                    sendBroadcast(i1);
                }
            }
            else if(intent.getStringExtra("command").equals(Notif2WatchConstants.EXTRAS.COMMAND_REFRESH_WATCHFACE)){
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, ifilter);
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int percent = (int)((level*100) / (float)scale);

                if (percent >= 0) {
                    Intent i2 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.BATTERY_CHANGED);
                    i2.putExtra(Notif2WatchConstants.EXTRAS.BATTERY_LEVEL, percent);
                    sendBroadcast(i2);

                    //PebbleNotification.sendBatteryToWatchface(context, level);
                }

                Intent i3 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_CHANGED);
                i3.putExtra(Notif2WatchConstants.EXTRAS.TUPLE_VALUES, getTupleValues());
                sendBroadcast(i3);

                Intent i4 = new  Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
                i4.putExtra("notification_event","Phone battery: " + String.valueOf(percent) + "% (" + String.valueOf(level) + "/" + String.valueOf(scale) + ")");
                sendBroadcast(i4);
            }
        }
    }
}
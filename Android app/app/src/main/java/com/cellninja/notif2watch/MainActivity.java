package com.cellninja.notif2watch;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView txtView;
    private EditText n2wKeyEdit;
    private ImageView imageView;
    private NotificationReceiver nReceiver;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 99;
    private int debugClickCount = 0;
    private boolean debugEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });
        */

        txtView = (TextView) findViewById(R.id.textView);
//        n2wKeyEdit = (EditText) findViewById(R.id.n2w_key_edit);
//
//        SharedPreferences sharedPref = getSharedPreferences(Notif2WatchConstants.DEFAULT_VALUES.PREFS_FILE_NAME, Context.MODE_PRIVATE);
//        String defaultValue = "";
//        String n2wKey = sharedPref.getString(Notif2WatchConstants.SHARED_PREFS_KEYS.N2W_KEY, defaultValue); // 1483848884889
//        n2wKeyEdit.setText(n2wKey);
//
//        n2wKeyEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                SharedPreferences sharedPref = getSharedPreferences(Notif2WatchConstants.DEFAULT_VALUES.PREFS_FILE_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString(Notif2WatchConstants.SHARED_PREFS_KEYS.N2W_KEY, s.toString());
//                editor.commit();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_UI);
        registerReceiver(nReceiver,filter);

        Intent startIntent = new Intent(MainActivity.this, Notif2WatchService.class);
        startIntent.setAction(Notif2WatchConstants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);

        /*
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean defaultValue = true;
        boolean firstTime = sharedPref.getBoolean(Notif2WatchConstants.SHARED_PREFS_KEYS.FIRST_TIME, defaultValue);
        if (firstTime) {
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Notif2WatchConstants.SHARED_PREFS_KEYS.FIRST_TIME, false);
            editor.commit();
        }
        */

        //Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_install_watchface) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ToastClass.showToast(this, "Access to storage is needed to pass the watchface to Pebble app.");
                } else {

                    // No explanation needed, we can request the permission.

                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
            else {
                installN2w();
            }
            return true;
        }
        else if (id == R.id.action_disable_notif2watch_notification) {
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void installN2w() {
        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("pebble://appstore/52b5a51020fef4a5c9000019"));  // change the uuid
        startActivity(intent);
         */
        final String WATCHAPP_FILENAME = "n2w.pbw";
        final String PEBBLE_LAUNCH_COMPONENT = "com.getpebble.android.basalt";
        final String PEBBLE_LAUNCH_ACTIVITY = "com.getpebble.android.ui.UpdateActivity";

        Log.i(this.getClass().getName(), "** Installing watchface **");
        InputStream input = null;
        OutputStream output = null;
        try {
            input = this.getApplicationContext().getAssets().open(WATCHAPP_FILENAME);
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dest = new File(path, "temporary_file.pbw");

            // delete existing file
            dest.delete();
            output = new FileOutputStream(dest);

            // copy asset to file
            byte[] buffer = new byte[2056];
            int length;
            while ((length = input.read(buffer))>0){
                output.write(buffer, 0, length);
            }
            output.flush();

            // launch pebble update activity
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.fromFile(dest));
            intent.setClassName(PEBBLE_LAUNCH_COMPONENT, PEBBLE_LAUNCH_ACTIVITY);
            try {
                this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    installN2w();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ToastClass.showToast(this, "Access to storage is needed to pass the watchface to Pebble app.");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void textViewClicked(View v) {
        debugClickCount++;
        if (debugClickCount == 5) {
            debugEnabled = !debugEnabled;
            txtView.setVisibility(debugEnabled ? View.VISIBLE : View.GONE);
            Button installWatchfaceButton = (Button) findViewById(R.id.install_watchface_button);
            installWatchfaceButton.setText(debugEnabled ? "List all notifications" : "Install N2W watchface");
            Button enableNotificationAccessButton = (Button) findViewById(R.id.notification_access_button);
            enableNotificationAccessButton.setText(debugEnabled ? "Add notification" : "Enable Notification Access");
            debugClickCount = 0;
        }
    }

    public void buttonClicked(View v){
        /*
        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("Notif2Watch");
            ncomp.setContentText("Notification example");
            ncomp.setTicker("Notification example");
            ncomp.setSmallIcon(R.mipmap.notif2watch_status_bar);

            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnClearNotify){
            Intent i = new Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_SERVICE);
            i.putExtra("command","clearall");
            sendBroadcast(i);
        }
        else if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_SERVICE);
            i.putExtra("command","list");
            sendBroadcast(i);
        }
        */
        if(v.getId() == R.id.notification_access_button){
            if (!debugEnabled) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
            else {
                NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
                ncomp.setContentTitle("Notif2Watch");
                ncomp.setContentText("Notification example");
                ncomp.setTicker("Notification example");
                ncomp.setSmallIcon(R.mipmap.notif2watch_status_bar);

                ncomp.setAutoCancel(true);
                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
            }
        }
        else if (v.getId() == R.id.install_watchface_button) {
            if (!debugEnabled) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("pebble://appstore/57f8a6fa56fda5acf1000094"));  // change the uuid
                    startActivity(intent);
                }
                catch (Exception e) {
                    ToastClass.showToast(this, "You need to have Pebble app installed to install N2W watchface.");
                }
            }
            else {
                Intent i = new Intent(Notif2WatchConstants.DEFAULT_VALUES.NOTIFICATION_LISTENER_SERVICE);
                i.putExtra("command","list");
                sendBroadcast(i);
            }

            /*
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("Notif2Watch");
            ncomp.setContentText("Notification example");
            ncomp.setTicker("Notification example");
            ncomp.setSmallIcon(R.mipmap.notif2watch_status_bar);

            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
            */
        }
        else if (v.getId() == R.id.disable_notif2watch_notifications_button) {
            try {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 21) {
                    intent.setClassName("com.android.settings", "com.android.settings.Settings$AppNotificationSettingsActivity");
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                }
                else {
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                }
                startActivity(intent);
            }
            catch (Exception e) {
                ToastClass.showToast(this, "Can't open Settings app.");
            }
        }
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (debugEnabled) {
                String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
                txtView.setText(temp);
            }
        }
    }
}

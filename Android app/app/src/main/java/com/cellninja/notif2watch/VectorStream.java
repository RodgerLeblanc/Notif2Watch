package com.cellninja.notif2watch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;

/**
 * Created by Roger on 2017-01-08.
 */

public class VectorStream {
    private static String TAG = "VectorStream";

    public VectorStream() {}

    public void sendToVectorStream(Context context, int[] tupleValues, String n2wKey) {
        Log.i(TAG, "sendToVectorStream(): " + n2wKey + " " + tupleValues.length);

        if (tupleValues.equals(null)) { return; }

        ArrayList<String> apps = new ArrayList<String>();
        for (int tupleValue : tupleValues) {
            Log.i(TAG, "tupleValue: " + tupleValue);
            if ((tupleValue >= 0) && (apps.size() < 8)) {
                apps.add(getAppNameFromValue(tupleValue));
            }
        }
        Log.i(TAG, "apps: " + apps.toString());

//        if (apps.size() > 0) {
            String streamText = TextUtils.join(" ", apps);
            if (streamText.isEmpty()) streamText = " ";
            new CallWebhook().execute(n2wKey, streamText);
//        }
    }

    private String getAppNameFromValue(int value) {
        Log.i(TAG, "getAppNameFromValue(): " + value);
        Log.i(TAG, "Notif2WatchConstants.APP_NAMES[value]: " + Notif2WatchConstants.APP_NAMES[value]);

        return Notif2WatchConstants.APP_NAMES[value];
    }

    private class CallWebhook extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection urlConnection = null;
            try {
                String n2wKey = params[0];
                String streamText = URLEncoder.encode(params[1], "utf-8");
                URL url = new URL("https://endpoint.vector.watch/VectorCloud/rest/v1/stream/168831F3329B917BA94173DB6A5DBDFD/webhook?msg=updateStream&n2wKey=" + n2wKey + "&streamText=" + streamText);
                urlConnection = (HttpsURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "Sending 'GET' request to URL : " + url);
                Log.i(TAG, "Response Code : " + responseCode);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String inputLine;
                StringBuffer response = new StringBuffer();

                int c;
                while ((c = in.read()) != -1) {
                    response.append(c);
                }
                in.close();

                Log.i(TAG, response.toString());
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

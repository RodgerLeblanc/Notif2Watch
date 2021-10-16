package com.cellninja.notif2watch;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Roger on 2015-10-27.
 */
public class ToastClass {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

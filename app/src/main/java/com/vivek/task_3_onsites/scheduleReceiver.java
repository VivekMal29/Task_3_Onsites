package com.vivek.task_3_onsites;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class scheduleReceiver extends BroadcastReceiver {

    SharedPreferences preferences;
    int hours;
    int minutes;

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        preferences = context.getSharedPreferences("STORAGE",
                Context.MODE_PRIVATE);
        hours = preferences.getInt("hour", 1);
        minutes = preferences.getInt("minutes", 1);
        Log.d("hello","hello");
        Log.d("hello", String.valueOf(minutes));
        Log.d("hello", String.valueOf(c.get(Calendar.MINUTE)));
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED ) {
            Log.d("hello","hello");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(preferences.getString("phone", ""), null, preferences.getString("message", ""), null, null);

        }
    }
}

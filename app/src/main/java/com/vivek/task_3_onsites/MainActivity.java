package com.vivek.task_3_onsites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {

    EditText phoneNo;
    EditText message;
    Button send;
    TimePicker timePicker;
    Calendar c;
    scheduleReceiver scheduleReceiver;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNo = findViewById(R.id.phoneNo);
        message = findViewById(R.id.txtMsg);
        send = findViewById(R.id.send);
        timePicker = findViewById(R.id.timePicker);


        preferences = this.getSharedPreferences("STORAGE",
                Context.MODE_PRIVATE);
        editor = preferences.edit();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sms", String.valueOf(phoneNo.getText()));
                Log.d("sms", String.valueOf(message.getText()));
                if(String.valueOf(phoneNo.getText()).equals("")||String.valueOf(message.getText()).equals("")||c==null){
                    Toast.makeText(MainActivity.this, "Please Complete all requirements", Toast.LENGTH_SHORT).show();
                }
                else{
                    ScheduleSms();
                    editor.putString("message", String.valueOf(message.getText()));
                    editor.putString("phone", String.valueOf(phoneNo.getText()));
                    editor.apply();
                }



            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE,minute);
                c.set(Calendar.SECOND,0);
                editor.putInt("hours",hourOfDay);
                editor.putInt("minutes",minute);

                if (c.before(Calendar.getInstance())) {
                    c.add(Calendar.DATE, 1);
                }

            }
        });


    }

    private void ScheduleSms() {

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,scheduleReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }


        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }

    private void sendSMS() {


        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(String.valueOf(phoneNo.getText()), null, String.valueOf(message.getText()), null, null);
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 0:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(MainActivity.this,
                            "You don't have required permissions", Toast.LENGTH_SHORT).show();
                }
        }
    }


}

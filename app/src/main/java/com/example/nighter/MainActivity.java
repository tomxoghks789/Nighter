package com.example.nighter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    public static int NOTIFICATION_ID = 930928;
    public static String CHANNEL_ID = "NOTI_CHANNEL";
    NotificationManager notificationManager;
    SeekBar brightSeekBar;
    Button enableBtn;
    Button disableBtn;
    NotificationCompat.Builder noti_builder;
    NotificationChannel notificationChannel;
    int importance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize_notification();
        brightSeekBar = findViewById(R.id.brightSeekBar);
        enableBtn = findViewById(R.id.runButton);
        disableBtn = findViewById(R.id.stopButton);
        enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noti_builder.setDefaults(Notification.DEFAULT_LIGHTS);
                noti_builder.setSmallIcon(R.drawable.noti_icon)
                        .setAutoCancel(false)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.ENABLE_MESSAGE));

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, launchIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                noti_builder.setContentIntent(contentIntent);
                notificationManager.notify(NOTIFICATION_ID, noti_builder.build());
            }
        });
        disableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(NOTIFICATION_ID);
                noti_builder.setSmallIcon(R.drawable.noti_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.DISABLE_MESSAGE));
                notificationManager.notify(NOTIFICATION_ID, noti_builder.build());
                notificationManager.cancel(NOTIFICATION_ID);
            }
        });
        brightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("KIM", String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void initialize_notification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (importance == 0) {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }
            notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            noti_builder = new NotificationCompat.Builder(MainActivity.this, notificationChannel.getId());
        }
    }
}
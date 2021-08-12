package com.example.nighter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    public static int NOTIFICATION_ID = 930928;
    NotificationManager notificationManager;
    NotificationCompat.Builder noti_builder;
    NotificationChannel notificationChannel;
    SeekBar brightSeekBar;
    Button enableBtn;
    Button disableBtn;
    int brightness = 0;
    int importance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!permissionCheck()) {
            startActivity(new Intent(getApplicationContext(), PermisionActivity.class));
            finish();
        }
        initialize_notification();
        brightSeekBar = findViewById(R.id.brightSeekBar);
        brightness = brightSeekBar.getProgress();
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
                toggleService();
            }
        });
        disableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationManager.getActiveNotifications().length != 0) {
                    notificationManager.cancel(NOTIFICATION_ID);
                    noti_builder.setSmallIcon(R.drawable.noti_icon)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(getString(R.string.DISABLE_MESSAGE));
                    notificationManager.notify(NOTIFICATION_ID, noti_builder.build());
                    notificationManager.cancel(NOTIFICATION_ID);
                } else {
                    Toast.makeText(getApplicationContext(), "Nighter isn't running now..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        brightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
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

    public boolean permissionCheck() {
        if (Settings.canDrawOverlays(this)) {
            return true;
        }
        return false;
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

    public void toggleService() {
        Intent intent = new Intent(getApplicationContext(), OverlayService.class);
        startService(intent);
    }
}
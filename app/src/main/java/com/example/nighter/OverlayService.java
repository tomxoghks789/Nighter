package com.example.nighter;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class OverlayService extends Service {
    LinearLayout overlayView;
    float brightness = 0.5f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RunOverlayService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(overlayView);
            Log.i("KIM", "Disabled!");
        }
    }

    public void RunOverlayService() {
        Log.i("KIM", "Running!");
        overlayView = new LinearLayout(this);
        int col = Color.parseColor("#000000");
        overlayView.setBackgroundColor(col);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int x = 0;
        int y = 0;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(width, height, x, y,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        overlayView.setAlpha(brightness);
        wm.addView(overlayView, params);
    }
}

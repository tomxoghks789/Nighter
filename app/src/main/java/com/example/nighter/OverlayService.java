package com.example.nighter;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
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
        }
    }

    public void RunOverlayService() {
        overlayView = new LinearLayout(this);
        overlayView.setBackgroundColor(Color.parseColor("#000000"));
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels + getNaviBarHeight() * 3;
        int height = metrics.heightPixels + getNaviBarHeight() * 3;
        int x = 0;
        int y = 0;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams((width > height) ? width : height, (width > height) ? width : height, x, y,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        overlayView.setAlpha(brightness);
        wm.addView(overlayView, params);
    }

    public int getNaviBarHeight() {
        int result = 0;
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

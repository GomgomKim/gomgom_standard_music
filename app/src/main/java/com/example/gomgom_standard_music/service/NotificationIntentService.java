package com.example.gomgom_standard_music.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class NotificationIntentService extends IntentService {
    public NotificationIntentService() {
        super("notificationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        switch (intent.getAction()) {
            case "left":
                Handler leftHandler = new Handler(Looper.getMainLooper());
                leftHandler.post(() -> Toast.makeText(getBaseContext(), "You clicked the left button", Toast.LENGTH_LONG).show());
                Log.i("gomgom", "left");
                break;
            case "right":
                Handler rightHandler = new Handler(Looper.getMainLooper());
                rightHandler.post(() -> Toast.makeText(getBaseContext(), "You clicked the right button", Toast.LENGTH_LONG).show());
                Log.i("gomgom", "right");
                break;
        }
    }
}

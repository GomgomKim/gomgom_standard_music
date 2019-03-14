package com.example.gomgom_standard_music.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoading();
        setGIF();
    }

    public void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    public void setGIF(){
        /*ImageView loading_gif = (ImageView)findViewById(R.id.loading_gif);
        Glide.with(this).load().into(loading_gif);*/
    }
}

package com.example.gomgom_standard_music.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationIntentService extends BroadcastReceiver {

    Context context;
    int musicState; // 1000의자리 : 버튼 종류 (1:prev 2:play 3:next)  100의자리 : play상태 (0:stop 1:play) 1의자리~10의자리 : music index
    int button_state;
    int is_play;
    int music_index;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        musicState = intent.getExtras().getInt("musicState");
        button_state = musicState/1000;
        is_play = (musicState - button_state*1000)/100;
        music_index = musicState - button_state*1000 - is_play*100;

        Log.i("gomgomKim", String.valueOf(musicState));
        Log.i("gomgomKim", String.valueOf(button_state));
        Log.i("gomgomKim", String.valueOf(is_play));
        Log.i("gomgomKim", String.valueOf(music_index));



        switch (button_state){
            case 1: // prev
                if(music_index == 0){
                    music_index = 7;
                } else{
                    music_index--;
                }
                music_stop(context, music_index);
                music_play(context, music_index);
                break;
            case 2: // play
                if(is_play == 1){
                    music_pause(context);
                } else{
                    music_play(context, music_index);
                }
                break;
            case 3: // next
                if(music_index == 7){
                    music_index = 0;
                } else{
                    music_index++;
                }
                music_stop(context, music_index);
                music_play(context, music_index);
                break;
            case 9:
                manager.cancel(9);
                break;
        }

    }


    public void music_stop(Context context, int music_index){
        Intent intent_service_stop = new Intent(context, MusicService.class);
        intent_service_stop.putExtra("index", music_index);
        intent_service_stop.putExtra("state", "stop");
        context.startService(intent_service_stop);
    }

    public void music_play(Context context, int music_index){
        Intent intent_service_play = new Intent(context, MusicService.class);
        intent_service_play.putExtra("index", music_index);
        intent_service_play.putExtra("state", "play");
        context.startService(intent_service_play);
    }

    public void music_pause(Context context){
        Intent intent_service_play = new Intent(context, MusicService.class);
        intent_service_play.putExtra("state", "pause");
        context.startService(intent_service_play);
    }

}

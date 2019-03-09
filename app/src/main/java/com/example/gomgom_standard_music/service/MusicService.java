package com.example.gomgom_standard_music.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.events.DurationEvent;
import com.example.gomgom_standard_music.events.FinishMusicEvent;
import com.example.gomgom_standard_music.events.GetSongPlayInfoEvent;
import com.example.gomgom_standard_music.events.IsPlayEvent;
import com.example.gomgom_standard_music.events.SeekbarEvent;
import com.example.gomgom_standard_music.events.TimerEvent;

import java.util.Timer;
import java.util.TimerTask;



public class MusicService extends Service {

    MediaPlayer mp;
    int pos=0;
    int play_mode=0;
    int music_index=0;

    Timer timer, timer_update;
    int current_time = 0;
    boolean is_timer_on = false;

    private final IBinder mBinder = new LocalBinder();



    @Override
    public void onCreate() {
        super.onCreate();
        mp = changeMusicPlayer(0); //mp 초기화
    }


    class MusicTimer extends TimerTask{
        @Override
        public void run() {
            current_time++;
            BusProvider.getInstance().post(new TimerEvent(current_time));
        }
    }

    class PageUpdateTimer extends TimerTask{
        @Override
        public void run() {
            try{
                BusProvider.getInstance().post(new SeekbarEvent(mp.getCurrentPosition()));
                BusProvider.getInstance().post(new GetSongPlayInfoEvent(mp.isPlaying(), mp.getDuration(), music_index));
            } catch (IllegalStateException e){
                Log.i("gomgom", "illegal accessed");
            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  mBinder;
    }

    public class LocalBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        music_index = intent.getExtras().getInt("index", 0);
        String state = intent.getExtras().getString("state");
        int seekBarPosition = intent.getExtras().getInt("seekBar_position", -1);

        switch (state){
            case "play":
                setNotification();

                mp = changeMusicPlayer(music_index);
                mp.seekTo(pos);
                mp.setLooping(false);
                if(seekBarPosition != -1){
                    mp.seekTo(seekBarPosition);
                    current_time = seekBarPosition/1000;
                }
                if(!mp.isPlaying()) mp.start();

                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));

                timer = new Timer();
                timer_update = new Timer();
                timer.schedule(new MusicTimer(), 1000, 1000);
                timer_update.schedule(new PageUpdateTimer(), 300, 300);
                is_timer_on = true;

                break;

            case "stop":
                if(mp.isPlaying()){
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제
                    mp = changeMusicPlayer(music_index);
                    pos = 0;
                }
                current_time = 0;
                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                if(is_timer_on){
                    timer.cancel();
                    timer_update.cancel();
                    is_timer_on = false;
                }
                break;

            case "pause":
                pos = mp.getCurrentPosition();
                mp.pause();
                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                if(is_timer_on){
                    timer.cancel();
                    timer_update.cancel();
                    is_timer_on = false;
                }
                break;

            case "play_mode":
                play_mode = intent.getExtras().getInt("play_mode", 0);
                break;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()){
            mp.stop(); // 멈춤
            mp.reset();
            mp.release(); // 자원 해제
        }
    }

    public void setNextPlay(){
        mp.setOnCompletionListener(mediaPlayer -> {
            current_time = 0;
            switch (play_mode){
                case 0: // 전체반복
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제

                    if(music_index == 7){
                        mp = changeMusicPlayer(0);
                    } else{
                        music_index++;
                        mp = changeMusicPlayer(music_index);
                    }
                    mp.setLooping(false);
                    mp.start();

                    BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                    BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                    BusProvider.getInstance().post(new FinishMusicEvent(0));
                    break;

                case 1: // 한곡반복
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제
                    mp = changeMusicPlayer(music_index);
                    mp.start();
                    BusProvider.getInstance().post(new FinishMusicEvent(1));
                    break;

                case 2: // 반복없음
                    if(mp.isPlaying()){
                        mp.stop(); // 멈춤
                        mp.reset();
                        mp.release(); // 자원 해제
                    }
                    if(is_timer_on){
                        timer.cancel();
                        timer_update.cancel();
                        is_timer_on = false;
                    }
                    BusProvider.getInstance().post(new FinishMusicEvent(2));
                    break;
            }
        });
    }

    public void setNotification() {
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        customView.setImageViewResource(R.id.img_noti, R.drawable.booklet_img_01);
        customView.setTextViewText(R.id.title_noti, "bermuda");

        Intent leftIntent = new Intent(this, NotificationIntentService.class);
        leftIntent.setAction("left");
        customView.setOnClickPendingIntent(R.id.music_prev,
                PendingIntent.getActivity(this, 0,
                        leftIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        Intent rightIntent = new Intent(this, NotificationIntentService.class);
        rightIntent.setAction("right");
        customView.setOnClickPendingIntent(R.id.music_next,
                PendingIntent.getActivity(this, 0,
                        rightIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.booklet_img_01)
                        .setCustomContentView(customView);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }


    public MediaPlayer changeMusicPlayer(int index){
        switch(index){
            case 0:
                mp = MediaPlayer.create(this, R.raw.bermuda_triangle);
                break;
            case 1:
                mp = MediaPlayer.create(this, R.raw.she_s_a_baby);
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.artist);
                break;
            case 3:
                mp = MediaPlayer.create(this, R.raw.bermuda_triangle);
                break;
            case 4:
                mp = MediaPlayer.create(this, R.raw.she_s_a_baby);
                break;
            case 5:
                mp = MediaPlayer.create(this, R.raw.artist);
                break;
            case 6:
                mp = MediaPlayer.create(this, R.raw.bermuda_triangle);
                break;
            case 7:
                mp = MediaPlayer.create(this, R.raw.she_s_a_baby);
                break;
        }
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

        setNextPlay();

        return mp;
    }

}

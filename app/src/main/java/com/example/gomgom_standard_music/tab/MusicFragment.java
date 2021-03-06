
package com.example.gomgom_standard_music.tab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.MusicCustomPagerAdapter;
import com.example.gomgom_standard_music.events.DurationEvent;
import com.example.gomgom_standard_music.events.EndFragEvent;
import com.example.gomgom_standard_music.events.FinishMusicEvent;
import com.example.gomgom_standard_music.events.GetSongPlayInfoEvent;
import com.example.gomgom_standard_music.events.IsPlayEvent;
import com.example.gomgom_standard_music.events.SeekbarEvent;
import com.example.gomgom_standard_music.events.TimerEvent;
import com.example.gomgom_standard_music.interfaces.MainInterface;
import com.example.gomgom_standard_music.item.MusicInfoItem;
import com.example.gomgom_standard_music.main.MainActivity;
import com.example.gomgom_standard_music.service.BusProvider;
import com.example.gomgom_standard_music.service.MusicService;
import com.example.gomgom_standard_music.ui.VerticalViewPager;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by gomgomKim on 2019. 2. 11..
 */

public class MusicFragment extends Fragment
{
    @BindView(R.id.like_gif) ImageView like_gif;

    @BindView(R.id.btn_close) ImageButton btn_close;
    @BindView(R.id.play_status) LinearLayout play_status;

    @BindView(R.id.music_back_img) ImageView music_back_img;

    @BindView(R.id.heart_touch_area) LinearLayout heart_touch_area;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.singer) TextView singer;

    @BindView(R.id.btn_prevplay) ImageButton btn_prevplay;
    @BindView(R.id.btn_play) ImageButton btn_play;
    @BindView(R.id.btn_nextplay) ImageButton btn_nextplay;

    @BindView(R.id.btn_lyric) ImageButton btn_lyric;
    @BindView(R.id.heart) ImageButton heart;
    @BindView(R.id.heart_num) TextView heart_num;
    @BindView(R.id.btn_repeat) ImageButton btn_repeat;

    @BindView(R.id.currentTime) TextView currentTime;
    @BindView(R.id.maxTime) TextView maxTime;

    @BindView(R.id.musicPager)
    VerticalViewPager musicPager;

    @BindView(R.id.musicProgress) SeekBar seekBar; // 음악 재생위치를 나타내는 시크바

    @BindView(R.id.status1) ImageView status1;
    @BindView(R.id.status2) ImageView status2;
    @BindView(R.id.status3) ImageView status3;
    @BindView(R.id.status4) ImageView status4;
    @BindView(R.id.status5) ImageView status5;
    @BindView(R.id.status6) ImageView status6;
    @BindView(R.id.status7) ImageView status7;
    @BindView(R.id.status8) ImageView status8;



    final String TAG="MusicFragment";
    RelativeLayout layout;

    //음악 관련 변수
    int index=0;
    int pos; // 재생 멈춘 시점
    boolean isPlaying = false; // 재생중인지 확인할 변수

    int time=1;

    //가사 Popup
    TextView lyricsText;

    //음악리스트 Popup
    PopupWindow popup;

    //노래 제목 리스트
    ArrayList<String> musicarr;

    //like 갯수
    ArrayList<Integer> like_count;

    //auto play 할건지
    boolean isAutoPlay = false;

    //화면 새로고침은 처음만
    int refresh = 0;

    //곡 제목, 작곡, 작사, 편곡
    ArrayList<MusicInfoItem> music_info;

    //플레이모드 (0: 전체재생 1: 한곡재생 2: 반복없음)
    int play_mode = 0;

    //사용자 스크롤 방향
    boolean checkDirection, scrollStarted = false;

    //서비스 연결
    MusicService mService;
    boolean mBound = false;

    // 자동넘어감
    boolean auto_move = false;

    //하트 애니메이션
    private AnimationDrawable frameAnimation;


    public MusicFragment() {
        super();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, layout);
        getService();
        eventBus();
        makeData();
        initSetting();
//        makeStatus();
        makeSuppotStatus();
        initPlay();
        musicPagerSetting();
        seekBarSetting();
        playBtnSetting();
        lyrics_popupSetting();
        playmode();
        like_music();
        switch_music();
        return layout;
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().post(new EndFragEvent(isPlaying));
        BusProvider.getInstance().unregister(this);
//        ((MainInterface)getContext()).showMiniPlayer();
        super.onDestroy();
    }

    public void eventBus(){
        BusProvider.getInstance().register(this);
    }

    // 초기화
    @Subscribe
    public void FinishLoad(GetSongPlayInfoEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        seekBar.setMax(mEvent.getDuration());
        maxTime.setText(timeTranslation(mEvent.getDuration()/1000));
        isPlaying = mEvent.getIsPlay();

        musicPager.setCurrentItem(mEvent.getPosition());
        if(isPlaying){
            Glide.with(getContext()).load(R.drawable.play_btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        } else{
            Glide.with(getContext()).load(R.drawable.play_btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        }

        changeStatus();
    }

    // 플레이 이벤트
    @Subscribe
    public void FinishLoad(IsPlayEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        isPlaying = mEvent.isPlaying();
        if(isPlaying == true) { // 재생
            Glide.with(getContext()).load(R.drawable.play_btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);

        } else{ // 정지
            Glide.with(getContext()).load(R.drawable.play_btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        }
    }

    // 타이머 이벤트
    @Subscribe
    public void FinishLoad(TimerEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        currentTime.setText(timeTranslation(mEvent.getTime()));
    }

    // 음악 길이 이벤트
    @Subscribe
    public void FinishLoad(DurationEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        maxTime.setText(timeTranslation(mEvent.getDuration()/1000));
        seekBar.setMax(mEvent.getDuration());
    }

    // 시크바 이벤트
    @Subscribe
    public void FinishLoad(SeekbarEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        seekBar.setProgress(mEvent.getSeekPosition());
    }

    // 음악종료 이벤트
    @Subscribe
    public void FinishLoad(FinishMusicEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        switch (mEvent.getState()){
            case 0:
                isPlaying = true;
                auto_move = true;
                if(index == 7){
                    musicPager.setCurrentItem(0);
                } else{
                    musicPager.setCurrentItem(index+1);
                }
                seekBar.setProgress(0);
                currentTime.setText(timeTranslation(0));
                break;
            case 1:
                isPlaying = true;
                break;
            case 2:
                seekBar.setProgress(0);
                currentTime.setText(timeTranslation(0));
                Glide.with(getContext()).load(R.drawable.play_btn_play)
                        .apply(new RequestOptions().fitCenter()).into(btn_play);
                isPlaying = false;
                break;
        }
    }

    public void getService(){
        Intent intent = new Intent(getContext(), MusicService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }

    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSetting() {
        singer.setText("지코(ZICO)");

        heart.setTag(0); // 하트의 상태 / 0 : off / 1 : on
        title.setText(musicarr.get(0)); // 첫번째 노래제목
        heart_num.setText(String.valueOf(like_count.get(0))); // 첫번째 노래 하트개수

        Glide.with(getContext()).load(R.drawable.play_btn_prevplay)
                .apply(new RequestOptions().fitCenter()).into(btn_prevplay);
        Glide.with(getContext()).load(R.drawable.play_btn_nextplay)
                .apply(new RequestOptions().fitCenter()).into(btn_nextplay);
        btn_prevplay.setAlpha(0.5f); // 첫번째곡 이전버튼 반투명

        Glide.with(getContext()).load(R.drawable.play_btn_lyrics)
                .apply(new RequestOptions().fitCenter()).into(btn_lyric);

        if(isPlaying){
            Glide.with(getContext()).load(R.drawable.play_btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        } else{
            Glide.with(getContext()).load(R.drawable.play_btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        }


        Glide.with(getContext()).load(R.drawable.booklet_bottom)
                .apply(new RequestOptions().fitCenter()).into(music_back_img);


        currentTime.setText("00:00");

        btn_close.setOnClickListener(v -> {
            ((MainInterface)getContext()).removeFragment();
        });
        btn_close.bringToFront();
    }

    public void makeStatus(){
        for(int i=0; i<musicarr.size(); i++){
            ImageView status_img = new ImageView(getContext());
            Glide.with(getContext()).load(R.drawable.play_page_off)
                    .apply(new RequestOptions().fitCenter()).into(status_img);
            play_status.addView(status_img);
        }
    }

    public void makeSuppotStatus(){
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status1);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status2);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status3);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status4);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status5);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status6);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status7);
        Glide.with(getContext()).load(R.drawable.play_page_off)
                .apply(new RequestOptions().fitCenter()).into(status8);
    }

    public void changeStatus(){
        makeSuppotStatus();
        switch (index){
            case 0:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status1);
                break;
            case 1:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status2);
                break;
            case 2:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status3);
                break;
            case 3:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status4);
                break;
            case 4:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status5);
                break;
            case 5:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status6);
                break;
            case 6:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status7);
                break;
            case 7:
                Glide.with(getContext()).load(R.drawable.play_page_on)
                        .apply(new RequestOptions().fitCenter()).into(status8);
                break;
        }
    }

    public void initPlay(){
        auto_move = true;
        if(MainActivity.getState() == 0){
            index = MainActivity.getPosition();
            musicPager.setCurrentItem(index);
            title.setText(musicarr.get(index));
            music_stop();
            music_play();
        }
        MainActivity.setState(1);
    }

    public void musicPagerSetting(){
        musicPager.setAdapter(new MusicCustomPagerAdapter(getContext()));
        musicPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(checkDirection) {
                    if(positionOffset < 0.5f){ // 사용자가 아래서 위로 스와이프
                        if(position == musicarr.size()-1){ // 마지막 곡일 때
                            Toast.makeText(getContext(), "마지막 곡입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else { // 사용자가 위에서 아래로 스와이프

                    }
                    checkDirection = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                if(!auto_move){
                    if(isPlaying) music_stop();
                    music_play();
                }
                currentTime.setText(timeTranslation(0));
                seekBar.setProgress(0);
                auto_move = false;
                changePlay();
                title.setText(musicarr.get(position));
                setHeartNum(like_count.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if( !scrollStarted && state == VerticalViewPager.SCROLL_STATE_DRAGGING){
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });
    }

    public void seekBarSetting(){
        // 시크바 초기세팅
        seekBar.setProgress(0);

        // 시크바 색상 변경
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#80737373"), PorterDuff.Mode.SRC_IN);
//        seekBar.getThumb().setColorFilter(737373, PorterDuff.Mode.SRC_IN);

        // thumb 수정
        seekBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (seekBar.getHeight() > 0) {
                    Drawable thumb = getResources().getDrawable(R.drawable.play_page_on);
                    int h = seekBar.getMeasuredHeight();
                    int w = h;
                    Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
                    Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                    Drawable newThumb = new BitmapDrawable(getResources(), bmpScaled);
                    newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                    seekBar.setThumb(newThumb);
                    seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });

        // 시크바 세팅
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) { // 누르고 뗐을 때
                music_seek_play(seekBar.getProgress());
            }
            public void onStartTrackingTouch(SeekBar seekBar) { // 눌러서 움직일 때
                if(isPlaying) music_pause();
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    public void playBtnSetting(){ // 재생버튼
        btn_play.setOnClickListener(v -> {
            if(isPlaying) { // 재생중일 때
                music_pause();
            } else { // 정지일 때
                music_play();
            }
        });
    }

    public void music_play(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("state", "play");
        getActivity().startService(intent);
    }

    public void music_pause(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("state", "pause");
        getActivity().startService(intent);
    }

    public void music_stop(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("state", "stop");
        getActivity().startService(intent);
    }

    public void music_seek_play(int position){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("seekBar_position", position);
        intent.putExtra("state", "play");
        getActivity().startService(intent);
    }


    public void lyrics_popupSetting() {

        //팝업으로 띄울 커스텀뷰를 설정하고
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_lyrics, null);

        btn_lyric.setOnClickListener(v -> {

            //클릭시 팝업 윈도우 생성
            popup = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

            RelativeLayout popupLayout = (RelativeLayout)layout.findViewById(R.id.whole_layout);
            popup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);

            ImageButton btn_close = (ImageButton) popupView.findViewById(R.id.btn_close);
            TextView title = (TextView) popupView.findViewById(R.id.title);
            TextView info = (TextView) popupView.findViewById(R.id.info);
            title.setText(music_info.get(index).getMusic_title());
            info.setText(
                    "작곡 : "+music_info.get(index).getComposition()+"\n"+
                    "작사 : "+music_info.get(index).getWriter()+"\n"+
                    "편곡 : "+music_info.get(index).getArrangement()+"\n");

            btn_close.setOnClickListener(view -> popup.dismiss());

        });

        lyricsText=(TextView)popupView.findViewById(R.id.lyricsText);

        AssetManager am = getContext().getAssets();
        try {
            InputStream inputStream = am.open("first.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader br = new BufferedReader(inputStreamReader);

            String read=null;
            String lyrics="";

            while((read=br.readLine())!=null){
                lyrics+=read;
                lyrics+="\n";
            }

            lyricsText.setText(lyrics);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changePlay(){
        switch (index){
            case 0:
                btn_prevplay.setAlpha(0.5f);
                btn_nextplay.setAlpha(1f);
                break;
            case 7:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(0.5f);
                break;
            default:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(1f);
                break;
        }
    }


    // 가사
    public void changeLyrics(int index){
        AssetManager am = getContext().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read="";
        String lyrics="";
        try {
            inputStream = am.open("first.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read=br.readLine())!=null){
                lyrics+=read;
                lyrics+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*switch(index){
            case 0:
                try {
                    inputStream = am.open("first.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    inputStream = am.open("second.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    inputStream = am.open("third.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    inputStream = am.open("fourth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    inputStream = am.open("fifth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    inputStream = am.open("sixth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    inputStream = am.open("seventh.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    inputStream = am.open("first.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    inputStream = am.open("second.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    inputStream = am.open("third.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    inputStream = am.open("fourth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }*/

        lyricsText.setText(lyrics);


    }

    // 현재시간 나타내기
    public String timeTranslation(int time){
        int minutes=time/60;
        int second=time-minutes*60;
        String result="";
        result+=String.format("%02d", minutes);
        result+=":";
        result+=String.format("%02d", second);
        return result;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mBound){
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    // 전체재생, 1곡재생 눌렀을 때
    public void playmode(){
        btn_repeat.setTag("all");
        btn_repeat.setOnClickListener(view -> {
            if(view.getTag().equals("all")){
                btn_repeat.setBackgroundResource(R.drawable.play_btn_repeatone);
                btn_repeat.setTag("one");
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtra("play_mode", 1);
                intent.putExtra("state", "play_mode");
                getActivity().startService(intent);
            }else if(view.getTag().equals("one")){
                btn_repeat.setBackgroundResource(R.drawable.play_btn_repeatall);
                btn_repeat.setAlpha(0.5f);
                btn_repeat.setTag("none");
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtra("play_mode", 2);
                intent.putExtra("state", "play_mode");
                getActivity().startService(intent);
            }else if(view.getTag().equals("none")){
                btn_repeat.setAlpha(1f);
                btn_repeat.setTag("all");
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtra("play_mode", 0);
                intent.putExtra("state", "play_mode");
                getActivity().startService(intent);
            }
        });
    }

    // 하트 눌렀을때
    public void like_music(){
        heart_touch_area.setOnClickListener(view -> {
            if((Integer)heart.getTag() == 0){
                heart.setBackgroundResource(R.drawable.btn_like_on);
                int current_like_count = like_count.get(index)+1;
                setHeartNum(current_like_count);
                heart.setTag(1);
                viewGif();
            } else if ((Integer)heart.getTag() == 1){
                heart.setBackgroundResource(R.drawable.btn_like_off);
                int current_like_count = like_count.get(index)-1;
                setHeartNum(current_like_count);
                heart.setTag(0);
            }
        });
    }

    public void viewGif(){
        like_gif.setBackgroundResource(R.drawable.like);
        like_gif.bringToFront();
        frameAnimation = (AnimationDrawable) like_gif.getBackground();
        if(frameAnimation.isRunning()) frameAnimation.stop();
        frameAnimation.start();
    }


    // <- -> 버튼 눌렀을 때
    public void switch_music(){
        //다음
        btn_nextplay.setOnClickListener(view -> {
            if(index != musicarr.size()-1)
                musicPager.setCurrentItem(index+1);
            else
                Toast.makeText(getContext(), "마지막 곡입니다", Toast.LENGTH_SHORT).show();
        });

        //이전
        btn_prevplay.setOnClickListener(view -> {
            if(index !=0)
                musicPager.setCurrentItem(index-1);
        });
    }

    // 하트갯수 수정
    public void setHeartNum(int current_like_count){
        String heart_count = "";
        if(current_like_count >= 1000) {
            int thousand = current_like_count/1000;
            int rest = current_like_count - thousand*1000;
            if(rest/500 == 1) heart_count = (current_like_count/1000)+".5k";
            else heart_count = (current_like_count/1000)+"k";
        }
        else heart_count = String.valueOf(current_like_count);
        like_count.set(index, current_like_count);
        heart_num.setText(heart_count);
    }

    public void makeData(){
        // make title data - database 연동예정
        musicarr = new ArrayList<>();
        like_count = new ArrayList<>();
        musicarr.add("BERMUDA TRIANGLE");  like_count.add(13);
        musicarr.add("She's Baby");  like_count.add(1789);
        musicarr.add("Artist");   like_count.add(13542);
        musicarr.add("FANXY CHILD"); like_count.add(486);
        musicarr.add("천재(Behind the scene)");  like_count.add(992);
        musicarr.add("Okey Dokey");  like_count.add(22396);
        musicarr.add("나는 나 너는 너");  like_count.add(9);
        musicarr.add("SoulMate");  like_count.add(75);

        // make music info - database 연동예정
        music_info = new ArrayList<>();
        music_info.add(new MusicInfoItem("BERMUDA TRIANGLE", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("She's Baby", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("Artist", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("FANXY CHILD", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("천재(Behind the scene)", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("Okey Dokey", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("나는 나 너는 너", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
        music_info.add(new MusicInfoItem("SoulMate", "ZICO, Poptime", "ZICO", "ZICO, Poptime"));
    }


}

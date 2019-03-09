package com.example.gomgom_standard_music.tab;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.events.DurationEvent;
import com.example.gomgom_standard_music.events.GetSongPlayInfoEvent;
import com.example.gomgom_standard_music.events.IsPlayEvent;
import com.example.gomgom_standard_music.events.SeekbarEvent;
import com.example.gomgom_standard_music.service.BusProvider;
import com.example.gomgom_standard_music.service.MusicService;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiniPlayerFragment extends Fragment {

    @BindView(R.id.album_img) ImageView album_img;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.btn_prevplay) ImageButton btn_prevplay;
    @BindView(R.id.btn_play) ImageButton btn_play;
    @BindView(R.id.btn_nextplay) ImageButton btn_nextplay;
    @BindView(R.id.musicProgress) SeekBar seekBar;

    RelativeLayout layout;

    ArrayList<String> musicarr;
    ArrayList<Integer> albumarr;

    boolean isPlaying = false;
    int position=0;

    public MiniPlayerFragment() {
        // Required empty public constructor
    }

    // 초기화
    @Subscribe
    public void FinishLoad(GetSongPlayInfoEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        isPlaying = mEvent.getIsPlay();
        position = mEvent.getPosition();
        if(isPlaying){
            Glide.with(getContext()).load(R.drawable.play_btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        } else{
            Glide.with(getContext()).load(R.drawable.play_btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        }

        title.setText(musicarr.get(position));
        Glide.with(getContext()).load(albumarr.get(position))
                .apply(new RequestOptions().fitCenter().circleCrop()).into(album_img);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_mini_player, container, false);
        ButterKnife.bind(this, layout);

        dataSetting();
        initSetting();
        seekBarSetting();
        btnSetting();

        return layout;
    }

    public void initSetting(){
        BusProvider.getInstance().register(this);

        Glide.with(getContext()).load(albumarr.get(position))
                .apply(new RequestOptions().fitCenter().circleCrop()).into(album_img);
        title.setText(musicarr.get(position));


        Glide.with(getContext()).load(R.drawable.play_btn_prevplay)
                .apply(new RequestOptions().fitCenter()).into(btn_prevplay);
        Glide.with(getContext()).load(R.drawable.play_btn_play)
                .apply(new RequestOptions().fitCenter()).into(btn_play);
        Glide.with(getContext()).load(R.drawable.play_btn_nextplay)
                .apply(new RequestOptions().fitCenter()).into(btn_nextplay);

    }

    public void seekBarSetting(){
        // 시크바 초기세팅
        seekBar.setProgress(0);

        // 시크바 색상 변경
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#80737373"), PorterDuff.Mode.SRC_IN);
        seekBar.setThumb(null);
//        seekBar.getThumb().setColorFilter(737373, PorterDuff.Mode.MULTIPLY);

        // 터치제한
        seekBar.setOnTouchListener((v, event) -> true);

    }

    // 시크바 이벤트
    @Subscribe
    public void FinishLoad(SeekbarEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        seekBar.setProgress(mEvent.getSeekPosition());
    }

    // 음악 길이 이벤트
    @Subscribe
    public void FinishLoad(DurationEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        seekBar.setMax(mEvent.getDuration());
    }

    public void btnSetting(){
        btn_prevplay.setOnClickListener(view -> {
            if(position !=0){
                position --;
            } else{
                position = 7;
            }
            if(isPlaying) music_stop();
            music_start();
        });

        btn_play.setOnClickListener(view -> {
            if(isPlaying){
                music_pause();
                Glide.with(getContext()).load(R.drawable.play_btn_play)
                        .apply(new RequestOptions().fitCenter()).into(btn_play);
            } else{
                music_start();
                Glide.with(getContext()).load(R.drawable.play_btn_pause)
                        .apply(new RequestOptions().fitCenter()).into(btn_play);
            }

        });

        btn_nextplay.setOnClickListener(view -> {
            if(position != 7){
                position ++;
            } else{
                position = 0;
            }
            if(isPlaying) music_stop();
            music_start();
        });
    }

    public void music_pause(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", position);
        intent.putExtra("state", "pause");
        getActivity().startService(intent);
    }

    public void music_start(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", position);
        intent.putExtra("state", "play");
        getActivity().startService(intent);
    }

    public void music_stop(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", position);
        intent.putExtra("state", "stop");
        getActivity().startService(intent);
    }

    public void dataSetting(){
        musicarr = new ArrayList<>();
        albumarr = new ArrayList<>();
        musicarr.add("BERMUDA TRIANGLE");  albumarr.add(R.drawable.booklet_img_08);
        musicarr.add("She's Baby");albumarr.add(R.drawable.booklet_img_01);
        musicarr.add("Artist");albumarr.add(R.drawable.booklet_img_06);
        musicarr.add("FANXY CHILD");albumarr.add(R.drawable.booklet_img_08);
        musicarr.add("천재(Behind the scene)");albumarr.add(R.drawable.booklet_img_04);
        musicarr.add("Okey Dokey");albumarr.add(R.drawable.booklet_img_05);
        musicarr.add("나는 나 너는 너");albumarr.add(R.drawable.booklet_img_06);
        musicarr.add("SoulMate");albumarr.add(R.drawable.booklet_img_01);
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

}

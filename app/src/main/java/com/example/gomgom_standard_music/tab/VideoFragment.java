package com.example.gomgom_standard_music.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.VideoListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    @BindView(R.id.top_img) ImageView top_img;
    @BindView(R.id.video_list) ListView video_list;
    RelativeLayout layout = null;

    VideoListAdapter videoListAdapter;


    public VideoFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        adapterSetting();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.video_title).into(top_img);
    }

    public void adapterSetting(){
        videoListAdapter = new VideoListAdapter(getContext());
        videoListAdapter.addItem("She's a Baby 공식 뮤직비디오", "ohSpvSGXfhY", 20170321, 14473, R.drawable.btn_like_off, 120);
        videoListAdapter.addItem("ZICO 서울콘서트", "-oxMqTjWqTA", 940923, 2423, R.drawable.btn_like_off, 11);
        videoListAdapter.addItem("Artist 공식 뮤직비디오", "obzb7nlpXZ0", 123123, 5437, R.drawable.btn_like_off, 321);
        video_list.setAdapter(videoListAdapter);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                video_list.setSelection(0);
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }
}

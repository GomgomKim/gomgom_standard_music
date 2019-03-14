package com.example.gomgom_standard_music.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.gomgom_standard_music.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoverFragment extends Fragment {

    @BindView(R.id.top_cover_img) ImageView top_cover_img;
    @BindView(R.id.bottom_cover_img) ImageView bottom_cover_img;

    RelativeLayout layout = null;

    public CoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_cover, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.cover_1).into(top_cover_img);
        Glide.with(getContext()).load(R.drawable.cover_title).into(bottom_cover_img);
    }

}

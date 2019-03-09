package com.example.gomgom_standard_music.tab;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SnsFragment extends Fragment {

    @BindView(R.id.top_layout) ImageView top_layout;
    @BindView(R.id.middle_layout1) ImageView middle_layout1;
    @BindView(R.id.middle_layout2) ImageView middle_layout2;
    @BindView(R.id.btn_homepage) ImageButton btn_homepage;
    @BindView(R.id.sns_main) LinearLayout sns_main;

    @BindView(R.id.fb_layout) LinearLayout fb_layout;
    @BindView(R.id.insta_layout) LinearLayout insta_layout;
    @BindView(R.id.fb_layout2) LinearLayout fb_layout2;
    @BindView(R.id.twt_layout) LinearLayout twt_layout;
    @BindView(R.id.insta_layout2) LinearLayout insta_layout2;

    @BindView(R.id.fb_img) ImageView fb_img;
    @BindView(R.id.insta_img) ImageView insta_img;
    @BindView(R.id.fb_img2) ImageView fb_img2;
    @BindView(R.id.twt_img) ImageView twt_img;
    @BindView(R.id.insta_img2) ImageView insta_img2;


    RelativeLayout layout = null;

    public SnsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_sns, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        bannerSetting();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.sns_title).apply(new RequestOptions().fitCenter()).into(top_layout);
        Glide.with(getContext()).load(R.drawable.booklet_img_01).apply(new RequestOptions().fitCenter()).into(middle_layout1);
        Glide.with(getContext()).load(R.drawable.bg_gradation).apply(new RequestOptions().fitCenter()).into(middle_layout2);
        middle_layout2.bringToFront();
        sns_main.bringToFront();

        btn_homepage.setOnClickListener(view -> {

        });
    }

    public void bannerSetting(){
        Glide.with(getContext()).load(R.drawable.sns_fb_01).apply(new RequestOptions().fitCenter()).into(fb_img);
        Glide.with(getContext()).load(R.drawable.sns_isg_01).apply(new RequestOptions().fitCenter()).into(insta_img);
        Glide.with(getContext()).load(R.drawable.sns_fb_02).apply(new RequestOptions().fitCenter()).into(fb_img2);
        Glide.with(getContext()).load(R.drawable.sns_twt_01).apply(new RequestOptions().fitCenter()).into(twt_img);
        Glide.with(getContext()).load(R.drawable.sns_isg_02).apply(new RequestOptions().fitCenter()).into(insta_img2);

        btn_homepage.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/koz.entofficial")));
        });

        fb_layout.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ZICO777BLOCKB/")));
        });

        insta_layout.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/woozico0914/")));
        });

        fb_layout2.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/BlockBOfficial/")));
        });

        twt_layout.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ZICO92")));
        });

        insta_layout2.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/explore/tags/fancychild/")));
        });

    }

}

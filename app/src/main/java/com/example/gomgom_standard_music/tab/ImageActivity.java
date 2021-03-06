package com.example.gomgom_standard_music.tab;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.github.chrisbanes.photoview.PhotoView;


public class ImageActivity extends Activity {

    RelativeLayout whole_layout, top_layout, bottom_layout;
    RelativeLayout btn_close;
    PhotoView popup_img;

    RelativeLayout land_whole_layout, land_top_layout, land_bottom_layout;
    RelativeLayout land_btn_close;
    PhotoView land_popup_img;

    int image_resource = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getImageInfo();
        initSetting();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_image);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            // 세로 전환할 때 이미지 교체
            initSetting();
        } else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            // 가로 전환할 때 이미지 교체
            initSetting_land();
        }

    }


    public void getImageInfo(){
        Intent intent = getIntent();
        image_resource = intent.getExtras().getInt("img_resource", 0);
    }

    public void initSetting(){
        whole_layout = (RelativeLayout) findViewById(R.id.whole_layout);
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        btn_close = (RelativeLayout) findViewById(R.id.btn_close);
        popup_img = (PhotoView) findViewById(R.id.popup_img);


        // 이미지 넣기
        Glide.with(this).load(image_resource)
                .apply(new RequestOptions().fitCenter()).into(popup_img);
        popup_img.getLayoutParams().height = 3000;


        // title 나타내기
        whole_layout.setOnClickListener(view -> {
            if(top_layout.getVisibility() == View.GONE){
                top_layout.setVisibility(View.VISIBLE);
                bottom_layout.setBackgroundColor(Color.WHITE);
                popup_img.getLayoutParams().height = 1000;
            } else if(top_layout.getVisibility() == View.VISIBLE){
                top_layout.setVisibility(View.GONE);
                bottom_layout.setBackgroundColor(Color.BLACK);
                popup_img.getLayoutParams().height = 3000;
            }
        });


        popup_img.setOnClickListener(view -> {
            if(top_layout.getVisibility() == View.GONE){
                top_layout.setVisibility(View.VISIBLE);
                bottom_layout.setBackgroundColor(Color.WHITE);
                popup_img.getLayoutParams().height = 1000;
            } else if(top_layout.getVisibility() == View.VISIBLE) {
                top_layout.setVisibility(View.GONE);
                bottom_layout.setBackgroundColor(Color.BLACK);
                popup_img.getLayoutParams().height = 3000;
            }
        });

        // X 버튼
        btn_close.setOnClickListener(view -> {
            bottom_layout.setBackgroundColor(Color.BLACK);
            top_layout.setVisibility(View.GONE);
            finish();
        });
    }

    public void initSetting_land(){
        land_whole_layout = (RelativeLayout) findViewById(R.id.land_whole_layout);
        land_top_layout = (RelativeLayout) findViewById(R.id.land_top_layout);
        land_bottom_layout = (RelativeLayout) findViewById(R.id.land_bottom_layout);
        land_btn_close = (RelativeLayout) findViewById(R.id.land_btn_close);
        land_popup_img = (PhotoView) findViewById(R.id.land_popup_img);



        // 이미지 넣기
        Glide.with(this).load(image_resource)
                .apply(new RequestOptions().fitCenter()).into(land_popup_img);
        land_popup_img.getLayoutParams().width = 2500;

        // title 나타내기
        land_whole_layout.setOnClickListener(view -> {
            if(land_top_layout.getVisibility() == View.GONE){
                land_top_layout.setVisibility(View.VISIBLE);
                land_bottom_layout.setBackgroundColor(Color.WHITE);
                land_popup_img.getLayoutParams().width = 1500;
            } else if(land_top_layout.getVisibility() == View.VISIBLE){
                land_top_layout.setVisibility(View.GONE);
                land_bottom_layout.setBackgroundColor(Color.BLACK);
                land_popup_img.getLayoutParams().width = 2500;
            }
        });

        land_popup_img.setOnClickListener(view -> {
            if(land_top_layout.getVisibility() == View.GONE){
                land_top_layout.setVisibility(View.VISIBLE);
                land_bottom_layout.setBackgroundColor(Color.WHITE);
                land_popup_img.getLayoutParams().width = 1500;
            } else if(land_top_layout.getVisibility() == View.VISIBLE){
                land_top_layout.setVisibility(View.GONE);
                land_bottom_layout.setBackgroundColor(Color.BLACK);
                land_popup_img.getLayoutParams().width = 2500;
            }
        });

        // X 버튼
        land_btn_close.setOnClickListener(view -> {
            land_bottom_layout.setBackgroundColor(Color.BLACK);
            land_top_layout.setVisibility(View.GONE);
            finish();
        });
    }
}

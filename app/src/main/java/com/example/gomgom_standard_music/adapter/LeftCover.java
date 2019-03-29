package com.example.gomgom_standard_music.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeftCover extends Fragment {

    @BindView(R.id.profile_click) RelativeLayout profile_click;
    @BindView(R.id.profile_img) ImageView profile_img;
    NavigationView layout = null;

    public LeftCover() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (NavigationView) inflater.inflate(R.layout.fragment_left_cover, container, false);
        ButterKnife.bind(this, layout);

        profileSetting();

        return layout;
    }

    public void profileSetting(){
        Glide.with(getContext()).load(R.drawable.profile_sample).apply(new RequestOptions().circleCrop()).into(profile_img);

        profile_click.setOnClickListener(v -> {
            terms_popup_window_join(v);
        });
    }

    public void terms_popup_window(View v){
        View popupView;
        PopupWindow pw;

        LayoutInflater inf = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.change_user_info,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        ImageButton btn_close = (ImageButton)popupView.findViewById(R.id.btn_close);
        ImageView profile_img_detail = (ImageView)popupView.findViewById(R.id.profile_img_detail);
        Glide.with(getContext()).load(R.drawable.profile_sample).apply(new RequestOptions().circleCrop()).into(profile_img_detail);

        btn_close.setOnClickListener(view -> pw.dismiss());

        pw.showAsDropDown(v);
    }

    public void terms_popup_window_join(View v){
        View popupView;
        PopupWindow pw;

        LayoutInflater inf = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.popup_join,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        ImageButton btn_close = (ImageButton)popupView.findViewById(R.id.btn_close);
        ImageView hello_img = (ImageView)popupView.findViewById(R.id.hello_img);
        RelativeLayout kakao_area = (RelativeLayout)popupView.findViewById(R.id.kakao_area);
        RelativeLayout naver_area = (RelativeLayout)popupView.findViewById(R.id.naver_area);
        RelativeLayout facebook_area = (RelativeLayout)popupView.findViewById(R.id.facebook_area);
        RelativeLayout twt_area = (RelativeLayout)popupView.findViewById(R.id.twt_area);

        RelativeLayout fast_join = (RelativeLayout)popupView.findViewById(R.id.fast_join);

        Glide.with(getContext()).load(R.drawable.member_img_01).into(hello_img);

        btn_close.setOnClickListener(view -> pw.dismiss());

        //로그인
        kakao_area.setOnClickListener(view -> pw.dismiss());
        naver_area.setOnClickListener(view -> pw.dismiss());
        facebook_area.setOnClickListener(view -> pw.dismiss());
        twt_area.setOnClickListener(view -> pw.dismiss());

        //빠른 회원 가입
        fast_join.setOnClickListener(view -> terms_popup_window_fast_join(v));

        pw.showAsDropDown(v);
    }

    public void terms_popup_window_fast_join(View v){
        View popupView;
        PopupWindow pw;

        LayoutInflater inf = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.popup_quick_join,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        ImageButton btn_close = (ImageButton)popupView.findViewById(R.id.btn_close);
        ImageView hello_img = (ImageView)popupView.findViewById(R.id.hello_img);

        TextView user_name = (TextView)popupView.findViewById(R.id.user_name);
        TextView user_id = (TextView)popupView.findViewById(R.id.user_id);
        TextView user_password = (TextView)popupView.findViewById(R.id.user_password);
        TextView user_password_check = (TextView)popupView.findViewById(R.id.user_password_check);
        TextView user_phone_num = (TextView)popupView.findViewById(R.id.user_phone_num);
        TextView user_nickname = (TextView)popupView.findViewById(R.id.user_nickname);

        Glide.with(getContext()).load(R.drawable.member_img_01).into(hello_img);

        btn_close.setOnClickListener(view -> pw.dismiss());


        pw.showAsDropDown(v);
    }


}

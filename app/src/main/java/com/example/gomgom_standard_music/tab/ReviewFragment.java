package com.example.gomgom_standard_music.tab;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.ReviewListAdapter;
import com.example.gomgom_standard_music.item.ReviewListItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    @BindView(R.id.top_layout) ImageView top_layout;
    @BindView(R.id.middle_layout1) ImageView middle_layout1;
    @BindView(R.id.middle_layout2) ImageView middle_layout2;

    @BindView(R.id.title_layout) LinearLayout title_layout;
    @BindView(R.id.review_title) TextView review_title;
    @BindView(R.id.artist_sign) ImageView artist_sign;

    @BindView(R.id.count_layout) RelativeLayout count_layout;
    @BindView(R.id.comment_layout) RelativeLayout comment_layout;
    @BindView(R.id.btn_comment) ImageView btn_comment;
    @BindView(R.id.comment_count) TextView comment_count;

    @BindView(R.id.like_layout) LinearLayout like_layout;
    @BindView(R.id.like_img) ImageView like_img;
    @BindView(R.id.like_count) TextView like_count;

    @BindView(R.id.order_by_recent) LinearLayout order_by_recent;
    @BindView(R.id.order_by_like_count) LinearLayout order_by_like_count;
    @BindView(R.id.write_comment) LinearLayout write_comment;
    @BindView(R.id.btn_write) ImageView btn_write;

    @BindView(R.id.comment_list) ListView comment_list;

    @BindView(R.id.recent) TextView recent;
    @BindView(R.id.like) TextView like;

    RelativeLayout layout = null;

    boolean is_like = false;

    ReviewListAdapter reviewListAdapter;

    String list_state = "최신순";

    //Data Setting - DB세팅 후 없어짐
    private ArrayList<ReviewListItem> mItems = new ArrayList<>();
    private int position = 0;
    ArrayList<String> user_name_arr;
    ArrayList<String> date_arr;
    ArrayList<String> comment_text_arr;
    ArrayList<Integer> like_count_arr;
    ArrayList<Integer> heart_arr;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        orderSetting();
        dataSetting(); // DB세팅 후 없어짐
        listSetting();
        return layout;
    }

    public void initSetting(){
        SpannableString content = new SpannableString("최신순");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        recent.setText(content);
        recent.setTypeface(recent.getTypeface(), Typeface.BOLD);

        Glide.with(this).load(R.drawable.review_title)
                .apply(new RequestOptions().fitCenter()).into(top_layout);
        Glide.with(this).load(R.drawable.booklet_img_04)
                .apply(new RequestOptions().fitCenter()).into(middle_layout1);
        Glide.with(this).load(R.drawable.bg_gradation)
                .apply(new RequestOptions().fitCenter()).into(middle_layout2);
        Glide.with(this).load(R.drawable.review_artist_sign)
                .apply(new RequestOptions().fitCenter()).into(artist_sign);
        Glide.with(this).load(R.drawable.btn_comment)
                .apply(new RequestOptions().fitCenter()).into(btn_comment);
        Glide.with(this).load(R.drawable.btn_write)
                .apply(new RequestOptions().fitCenter()).into(btn_write);

        comment_count.setText("4");
        like_count.setText("120");
        like_layout.setOnClickListener(view -> {
            if(is_like){
                like_img.setBackgroundResource(R.drawable.btn_like_off);
                int count = Integer.parseInt(like_count.getText().toString()) -1;
                like_count.setText(String.valueOf(count));
                is_like = false;
            } else{
                like_img.setBackgroundResource(R.drawable.btn_like_on);
                int count = Integer.parseInt(like_count.getText().toString()) +1;
                like_count.setText(String.valueOf(count));
                is_like = true;
            }
        });

        middle_layout2.bringToFront();
        title_layout.bringToFront();
        count_layout.bringToFront();
    }

    public void orderSetting(){
        order_by_recent.setOnClickListener(view -> {
            list_state = "최신순";
            SpannableString content = new SpannableString("최신순");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            recent.setTypeface(recent.getTypeface(), Typeface.BOLD);
            like.setTypeface(like.getTypeface(), Typeface.NORMAL);
            recent.setText(content);
            like.setText("추천순");
            dataSetting();
            listSetting();
            reviewListAdapter.notifyDataSetChanged();
        });

        order_by_like_count.setOnClickListener(view -> {
            list_state = "추천순";
            SpannableString content = new SpannableString("추천순");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            recent.setTypeface(recent.getTypeface(), Typeface.NORMAL);
            like.setTypeface(like.getTypeface(), Typeface.BOLD);
            like.setText(content);
            recent.setText("최신순");
            dataSetting();
            listSetting();
            reviewListAdapter.notifyDataSetChanged();
        });
    }

    public void dataSetting(){
        user_name_arr = new ArrayList<>();
        date_arr = new ArrayList<>();
        comment_text_arr = new ArrayList<>();
        heart_arr = new ArrayList<>();
        like_count_arr = new ArrayList<>();

        if(list_state.equals("최신순")){
            user_name_arr.add("기연킴"); date_arr.add("2018.09.23"); comment_text_arr.add("얼른 나아서 지코공연 보러가야지 ~~");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(6742);

            user_name_arr.add("언어의마술사"); date_arr.add("2018.11.25"); comment_text_arr.add("발렌타인데이는 역시 솔초코");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(11);

            user_name_arr.add("romantic"); date_arr.add("2019.01.13"); comment_text_arr.add("지아코 ~!~! 넘모좋아 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(353);

            user_name_arr.add("gomgomKim"); date_arr.add("2019.02.21"); comment_text_arr.add("이번 스탠다드 앱은 지아코로 만들어서 얼마나 좋게요 ~! 두줄 이상 적으면 어떻게 되는지 확인하면 얼마나 좋게요 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(9);

        } else if(list_state.equals("추천순")){
            user_name_arr.add("기연킴"); date_arr.add("2018.09.23"); comment_text_arr.add("얼른 나아서 지코공연 보러가야지 ~~");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(6742);

            user_name_arr.add("romantic"); date_arr.add("2019.01.13"); comment_text_arr.add("지아코 ~!~! 넘모좋아 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(353);

            user_name_arr.add("언어의마술사"); date_arr.add("2018.11.25"); comment_text_arr.add("발렌타인데이는 역시 솔초코");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(11);

            user_name_arr.add("gomgomKim"); date_arr.add("2019.02.21"); comment_text_arr.add("이번 스탠다드 앱은 지아코로 만들어서 얼마나 좋게요 ~! 두줄 이상 적으면 어떻게 되는지 확인하면 얼마나 좋게요 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(9);
        }
    }

    public void listSetting(){
        reviewListAdapter = new ReviewListAdapter(getContext());
        for(int i = 0; i<user_name_arr.size(); i++){
            if(list_state.equals("최신순")) {
                if(i == 2){
                    reviewListAdapter.addItem(user_name_arr.get(i), date_arr.get(i), comment_text_arr.get(i), String.valueOf(R.drawable.booklet_img_03), heart_arr.get(i), like_count_arr.get(i));
                    continue;
                }
            } else if (list_state.equals("추천순")){
                if(i == 1){
                    reviewListAdapter.addItem(user_name_arr.get(i), date_arr.get(i), comment_text_arr.get(i), String.valueOf(R.drawable.booklet_img_03), heart_arr.get(i), like_count_arr.get(i));
                    continue;
                }
            }

            reviewListAdapter.addItem(user_name_arr.get(i), date_arr.get(i), comment_text_arr.get(i), "", heart_arr.get(i), like_count_arr.get(i));
        }

        comment_list.setAdapter(reviewListAdapter);
    }

}

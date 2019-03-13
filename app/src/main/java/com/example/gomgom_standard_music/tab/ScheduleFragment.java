package com.example.gomgom_standard_music.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.Schedule_sub;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    @BindView(R.id.top_layout) ImageView top_layout;
    @BindView(R.id.middle_layout1) ImageView middle_layout1;
    @BindView(R.id.middle_layout2) ImageView middle_layout2;
    @BindView(R.id.btn_prev) ImageButton btn_prev;
    @BindView(R.id.btn_next) ImageButton btn_next;
    @BindView(R.id.year) TextView year;
    @BindView(R.id.month) TextView month;
    @BindView(R.id.schedule_main) LinearLayout schedule_main;

    RelativeLayout layout = null;

    private int current_year, current_month;

    // 스케줄 갯수
    private int schedule_count=0;

    public ScheduleFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        adapterSetting();
        return layout;
    }

    public void initSetting(){
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        current_year = calendar.get(Calendar.YEAR);
        current_month = calendar.get(Calendar.MONTH) + 1;
        year.setText(String.valueOf(current_year)+".");
        month.setText(String.valueOf(String.format("%02d",current_month)));
        Glide.with(getContext()).load(R.drawable.schadule_title).apply(new RequestOptions().fitCenter()).into(top_layout);
        Glide.with(getContext()).load(R.drawable.booklet_img_03).apply(new RequestOptions().fitCenter()).into(middle_layout1);
        Glide.with(getContext()).load(R.drawable.schadule_bg).apply(new RequestOptions().fitCenter()).into(middle_layout2);
        middle_layout2.bringToFront();
        schedule_main.bringToFront();

        if(current_month == 2){
            btn_prev.setAlpha(0.5f);
            btn_next.setAlpha(1f);
        } else if (current_month == 3){
            btn_prev.setAlpha(1f);
            btn_next.setAlpha(0.5f);
        }

        btn_prev.setOnClickListener(view -> {
            if(current_month == 3){
                Toast.makeText(getContext(), "지나간 달로는 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                if(current_month == 1){
                    current_year--;
                    current_month = 12;
                } else{
                    current_month--;
                }
                year.setText(String.valueOf(current_year)+".");
                month.setText(String.valueOf(String.format("%02d",current_month)));
                schedule_main.removeAllViews();
                adapterSetting();
            }

            showSchedule();
        });

        btn_next.setOnClickListener(view -> {
            if(current_month == 7){
                Toast.makeText(getContext(), "3달 이후의 스케줄은 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else{
                if(current_month == 12){
                    current_year++;
                    current_month = 1;
                } else{
                    current_month++;
                }
                year.setText(String.valueOf(current_year)+".");
                month.setText(String.valueOf(String.format("%02d",current_month)));
                schedule_main.removeAllViews();
                adapterSetting();
            }

            showSchedule();
        });
    }

    public void showSchedule() {
        if(schedule_count == 0) schedule_main.setVisibility(View.GONE);
        else schedule_main.setVisibility(View.VISIBLE);
    }

    public void adapterSetting() {
        schedule_count = 0;
        createLayout(2019, 03, "13", "Sun", "환경 보호 축제", "3월 13일(일) 18:00", "여의나루 주차장", "방송");
        createLayout(2019, 03, "21", "Mon", "K2 팬싸인회", "3월 21일(월) 19:00", "영등포 타임스퀘어", "싸인회");
        createLayout(2019, 04, "26", "Sat", "유희열의 라디오 천국 크러쉬와 동반 출연", "4월 26일(토) 23:00", "KBS FM", "라디오");
    }

    public void createLayout(int year, int month, String day, String day_of_week, String what, String when, String where, String text){
        Schedule_sub schedule_sub = new Schedule_sub(getContext());
        schedule_sub.setData(year, month, day, day_of_week, what, when, where, text);
        if (current_year == schedule_sub.getYear() && current_month ==schedule_sub.getMonth()){
            schedule_count++;
            schedule_main.addView(schedule_sub);
        }

    }
}

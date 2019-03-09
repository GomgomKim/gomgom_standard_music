package com.example.gomgom_standard_music.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gomgom_standard_music.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Schedule_sub extends LinearLayout {

    @BindView(R.id.whole_layout) LinearLayout whole_layout;

    @BindView(R.id.day) TextView day;
    @BindView(R.id.day_of_week) TextView day_of_week;
    @BindView(R.id.what) TextView what;
    @BindView(R.id.when) TextView when;
    @BindView(R.id.where) TextView where;

    @BindView(R.id.bottom_layout) LinearLayout bottom_layout;
    @BindView(R.id.bottom_text) TextView bottom_text;

    LinearLayout layout;

    int year, month;

    public Schedule_sub(Context context) {
        super(context);
        initSetting(context);
    }

    public void initSetting(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.schedule_sub,this,true);
        ButterKnife.bind(layout);
    }

    public void setData(int year, int month, String day, String day_of_week, String what, String when, String where, String text){
        this.day.setText(day);
        this.day_of_week.setText(day_of_week);
        this.what.setText(what);
        this.when.setText(when);
        this.where.setText(where);
        this.bottom_text.setText(text);
        this.year = year;
        this.month = month;

        if(day_of_week.equals("Sun")){
            this.day.setTextColor(Color.parseColor("#f96f6c"));
            this.day_of_week.setTextColor(Color.parseColor("#f96f6c"));
            this.bottom_layout.setBackgroundColor(Color.parseColor("#f96f6c"));
        } else if(day_of_week.equals("Sat")){
            this.day.setTextColor(Color.parseColor("#4ba2c6"));
            this.day_of_week.setTextColor(Color.parseColor("#4ba2c6"));
            this.bottom_layout.setBackgroundColor(Color.parseColor("#4ba2c6"));
        } else{
            this.day.setTextColor(Color.parseColor("#4b555f"));
            this.day_of_week.setTextColor(Color.parseColor("#4b555f"));
            this.bottom_layout.setBackgroundColor(Color.parseColor("#4b555f"));
        }

//        whole_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public int getYear(){
        return this.year;
    }
    public int getMonth(){
        return this.month;
    }
}

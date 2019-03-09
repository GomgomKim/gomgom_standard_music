package com.example.gomgom_standard_music.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.item.ThanksToItem;

import java.util.ArrayList;

public class ThanksToListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;

    private ArrayList<ThanksToItem> mItems = new ArrayList<>();

    private ImageView profile_img;
    private TextView to_who;
    private TextView by_who;
    private TextView thanks_to_text;
    private ImageButton more_btn;

    private String text_backup;

    View popupView;
    PopupWindow pw;

    ThanksToItem current_item;


    public ThanksToListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ThanksToItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.thanks_to_list, parent, false);
            convertView.setTag(position);
        }

        profile_img = (ImageView) convertView.findViewById(R.id.profile_img);
        to_who = (TextView) convertView.findViewById(R.id.to_who);
        by_who = (TextView) convertView.findViewById(R.id.by_who);
        thanks_to_text = (TextView) convertView.findViewById(R.id.thanks_to_text);
        more_btn = (ImageButton) convertView.findViewById(R.id.more_btn);

        more_btn.setTag(position);
        thanks_to_text.setTag(position);

        if(current_item != null){
            if(position == 0){
                Glide.with(context).load(R.drawable.thanks_profile)
                        .apply(new RequestOptions().fitCenter().circleCrop()).into(profile_img);
                profile_img.setBackground(new ShapeDrawable(new OvalShape()));
                profile_img.setClipToOutline(true);
            } else{
                Glide.with(context).load(R.drawable.profile_empty)
                        .apply(new RequestOptions().fitCenter().circleCrop()).into(profile_img);
                profile_img.setBackground(new ShapeDrawable(new OvalShape()));
                profile_img.setClipToOutline(true);
            }

            to_who.setText("to "+current_item.getTo_who());
            by_who.setText("by "+current_item.getBy_who());
            if(current_item.getIs_long() == 0)
                current_item.setText(current_item.getText().substring(0, 200)+"...");
            thanks_to_text.setText(current_item.getText());
        }

        more_btn.setOnClickListener(view -> {
            Log.i("gomgom", String.valueOf(view.getTag()));
            if(String.valueOf(view.getTag()).equals("0")){
                terms_popup_window(view, R.drawable.thanks_profile, "FAN",
                        "ZICO", current_item.getText_backup());
            } else{
                terms_popup_window(view, R.drawable.profile_empty, current_item.getTo_who(),
                        current_item.getBy_who(), current_item.getText_backup());
            }
        });

        return convertView;
    }

    public void addItem(String to, String by, String text){
        ThanksToItem mItem = new ThanksToItem();
        mItem.setTo_who(to);
        mItem.setBy_who(by);
        mItem.setText(text);
        mItem.setText_backup(text);

        mItems.add(mItem);
    }

    public void terms_popup_window(View v, int img, String to, String by, String text){

        LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.thanks_to_more,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        // pw.setTouchable(true);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);

        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        ImageButton btn_close = (ImageButton)popupView.findViewById(R.id.btn_close);
        ImageView profile_img = (ImageView)popupView.findViewById(R.id.profile_img);
        TextView to_who = (TextView)popupView.findViewById(R.id.to_who);
        TextView by_who = (TextView)popupView.findViewById(R.id.by_who);
        TextView thanks_to_text = (TextView)popupView.findViewById(R.id.thanks_to_text);

        btn_close.setOnClickListener(view -> pw.dismiss());

        Glide.with(context).load(img)
                .apply(new RequestOptions().fitCenter().circleCrop()).into(profile_img);
        profile_img.setBackground(new ShapeDrawable(new OvalShape()));
        profile_img.setClipToOutline(true);

        to_who.setText("to "+to);
        by_who.setText("by "+by);
        thanks_to_text.setText(text);

        pw.showAsDropDown(v);
    }

}

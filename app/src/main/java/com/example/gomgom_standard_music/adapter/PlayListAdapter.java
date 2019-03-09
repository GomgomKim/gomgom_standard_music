package com.example.gomgom_standard_music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.interfaces.MainInterface;
import com.example.gomgom_standard_music.item.PlayListItem;
import com.example.gomgom_standard_music.main.MainActivity;

import java.util.ArrayList;

public class PlayListAdapter extends BaseAdapter {
    LayoutInflater inflater;

    private ArrayList<PlayListItem> mItems = new ArrayList<>();
    private LinearLayout heart_touch_area;
    private LinearLayout song_touch_area;
    private TextView index;
    private ImageView heart;
    private TextView heart_num;
    private ImageView title_img;
    private TextView title;
    private TextView singer;
    PlayListItem current_item;
    Context context;

    public PlayListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public PlayListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 뷰 종류
    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    // 뷰 여러개 붙이기
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.play_lists, parent, false);
        }
        index = (TextView) convertView.findViewById(R.id.index);
        heart_touch_area = (LinearLayout) convertView.findViewById(R.id.heart_touch_area);
        song_touch_area = (LinearLayout) convertView.findViewById(R.id.song_touch_area);
        heart = (ImageButton) convertView.findViewById(R.id.heart);
        heart_num = (TextView) convertView.findViewById(R.id.heart_num);
        title_img = (ImageView) convertView.findViewById(R.id.title_img);
        title = (TextView) convertView.findViewById(R.id.title);
        singer = (TextView) convertView.findViewById(R.id.singer);

        if(current_item != null){
            index.setText(current_item.getIndex());
            heart.setBackgroundResource(current_item.getHeart());

            if(current_item.getHeart_num()>=1000){
                heart_num.setText((current_item.getHeart_num()/1000)+"k");
            } else{
                heart_num.setText(String.valueOf(current_item.getHeart_num()));
            }

            title.setText(current_item.getTitle());
            singer.setText(current_item.getSinger());

            //set Image
            title_img.setImageResource(R.drawable.songlist_bl_title);

            if(current_item.getIsTitle() == 1){
                title_img.setVisibility(View.VISIBLE);
            } else if(current_item.getIsTitle() == 0){
                title_img.setVisibility(View.GONE);
            }

            // 하트 터치
            heart_touch_area.setOnClickListener(view -> {
                int is_heart = mItems.get(position).getHeart();
                if(is_heart == R.drawable.btn_like_off){
                    mItems.get(position).setHeart(R.drawable.btn_like_on);
                    mItems.get(position).setHeart_num(mItems.get(position).getHeart_num()+1);
                } else if (is_heart == R.drawable.btn_like_on) {
                    mItems.get(position).setHeart(R.drawable.btn_like_off);
                    mItems.get(position).setHeart_num(mItems.get(position).getHeart_num()-1);
                }
                notifyDataSetChanged();
            });

            // 곡명 터치
            song_touch_area.setOnClickListener(view -> {
                MainActivity.setPosition(position);
                MainActivity.setState(0);
                if(context instanceof MainInterface){
                    ((MainInterface)context).showMusicPlayer();
                }
            });
        }


        return convertView;
    }

    public void addItem(int type, int index, int heart, int heart_num, String title, String singer, int isTitle) {
        PlayListItem mItem = new PlayListItem();

        mItem.setType(type);
        mItem.setIndex(String.format("%02d", index));
        mItem.setHeart(heart);
        mItem.setHeart_num(heart_num);
        mItem.setTitle(title);
        mItem.setSinger(singer);
        mItem.setIsTitle(isTitle);

        mItems.add(mItem);

    }
}

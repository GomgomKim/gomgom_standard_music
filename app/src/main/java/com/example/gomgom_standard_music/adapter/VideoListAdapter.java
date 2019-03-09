package com.example.gomgom_standard_music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.item.VideoListItem;
import com.example.gomgom_standard_music.tab.VideoActivity;

import java.util.ArrayList;

public class VideoListAdapter extends BaseAdapter{
    LayoutInflater inflater;
    Context context;

    private ArrayList<VideoListItem> mItems = new ArrayList<>();
    private TextView video_title;

    private ImageView back_video_img;
    private ImageButton btn_video_play;

    private RelativeLayout bottom_layout;
    private TextView view_count;
    private ImageView btn_comment;
    private TextView comment_count;

    private LinearLayout like_layout;
    private ImageView like_img;
    private TextView like_count;

    VideoListItem current_item;


    public VideoListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public VideoListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.video_list, parent, false);
        }

        video_title = (TextView) convertView.findViewById(R.id.video_title);

        back_video_img = (ImageView) convertView.findViewById(R.id.back_video_img);
        btn_video_play = (ImageButton) convertView.findViewById(R.id.btn_video_play);

        bottom_layout = (RelativeLayout) convertView.findViewById(R.id.bottom_layout);
        view_count = (TextView) convertView.findViewById(R.id.view_count);
        btn_comment = (ImageView) convertView.findViewById(R.id.btn_comment);
        comment_count = (TextView) convertView.findViewById(R.id.comment_count);

        like_layout = (LinearLayout) convertView.findViewById(R.id.like_layout);
        like_img = (ImageView) convertView.findViewById(R.id.like_img);
        like_count = (TextView) convertView.findViewById(R.id.like_count);


        if(current_item != null){
            String url = "https://img.youtube.com/vi/"+current_item.getVideo_code()+"/0.jpg";
            Glide.with(context).load(url)
                    .apply(new RequestOptions().fitCenter()).into(back_video_img);
            Glide.with(context).load(R.drawable.btn_comment)
                    .apply(new RequestOptions().fitCenter()).into(btn_comment);
            like_img.setBackgroundResource(current_item.getHeart());
            video_title.setText(current_item.getTitle());
            view_count.setText(String.valueOf(current_item.getView_count()));
            like_count.setText(String.valueOf(current_item.getLike_count()));
            comment_count.setText(String.valueOf(current_item.getComment_count()));
        }

        btn_video_play.setOnClickListener(view -> {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("mItems", mItems);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        btn_comment.setOnClickListener(view -> {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("mItems", mItems);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        // 하트 터치
        like_layout.setOnClickListener(view -> {
            int is_heart = mItems.get(position).getHeart();
            if(is_heart == R.drawable.btn_like_off){
                mItems.get(position).setHeart(R.drawable.btn_like_on);
                mItems.get(position).setLike_count(mItems.get(position).getLike_count()+1);
            } else if (is_heart == R.drawable.btn_like_on) {
                mItems.get(position).setHeart(R.drawable.btn_like_off);
                mItems.get(position).setLike_count(mItems.get(position).getLike_count()-1);
            }
            notifyDataSetChanged();
        });


        return convertView;
    }

    public void addItem(String title, String video_code, int view_count, int comment_count, int heart, int like_count){
        VideoListItem mItem = new VideoListItem();

        mItem.setTitle(title);
        mItem.setVideo_code(video_code);
        mItem.setView_count(view_count);
        mItem.setComment_count(comment_count);
        mItem.setHeart(heart);
        mItem.setLike_count(like_count);

        mItems.add(mItem);
    }

    public void setItem(ArrayList<VideoListItem> arr){
        mItems = arr;
        notifyDataSetChanged();
    }
}

package com.example.gomgom_standard_music.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.item.ReviewListItem;

import java.util.ArrayList;

public class ReviewListAdapter extends BaseAdapter{
    LayoutInflater inflater;
    Context context;

    private ArrayList<ReviewListItem> mItems = new ArrayList<>();

    private ImageView profile_img;

    private TextView user_name;
    private TextView comment_date;

    private TextView comment_text;
    private ImageView comment_img;

    private RelativeLayout like_layout;
    private ImageView heart_img;
    private TextView like_count;

    ReviewListItem current_item;

    public ReviewListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ReviewListItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.video_comment, parent, false);
        }
        profile_img = (ImageView) convertView.findViewById(R.id.profile_img);

        user_name = (TextView) convertView.findViewById(R.id.user_name);
        comment_date = (TextView) convertView.findViewById(R.id.comment_date);

        comment_text = (TextView) convertView.findViewById(R.id.comment_text);
        comment_img = (ImageView) convertView.findViewById(R.id.comment_img);

        like_layout = (RelativeLayout) convertView.findViewById(R.id.like_layout);
        heart_img = (ImageView) convertView.findViewById(R.id.heart_img);
        like_count = (TextView) convertView.findViewById(R.id.like_count);

        if(current_item != null){
            Glide.with(context).load(R.drawable.profile_empty)
                    .apply(new RequestOptions().circleCrop()).into(profile_img);
            profile_img.setBackground(new ShapeDrawable(new OvalShape()));
            profile_img.setClipToOutline(true);

            user_name.setText(current_item.getUser_name());
            comment_date.setText(current_item.getDate());
            comment_text.setText(current_item.getComment_text());
            like_count.setText(String.valueOf(current_item.getLike_count()));

            if(!current_item.getComment_img().equals("")){
                Glide.with(context).load(Integer.parseInt(current_item.getComment_img()))
                        .apply(new RequestOptions().fitCenter()).into(comment_img);
                comment_img.setVisibility(View.VISIBLE);
            }

            Glide.with(context).load(current_item.getHeart())
                    .apply(new RequestOptions().fitCenter()).into(heart_img);
        }

        final int like_count = current_item.getLike_count();

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

    public void addItem(String user_name, String date, String comment_text, String comment_img, int heart, int like_count){
        ReviewListItem mItem = new ReviewListItem();

        mItem.setUser_name(user_name);
        mItem.setDate(date);
        mItem.setComment_text(comment_text);
        mItem.setComment_img(comment_img);
        mItem.setHeart(heart);
        mItem.setLike_count(like_count);

        mItems.add(mItem);
    }
}

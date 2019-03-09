package com.example.gomgom_standard_music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.item.BookletItem;
import com.example.gomgom_standard_music.tab.ImageActivity;

import java.util.ArrayList;

public class BookletListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;

    private ArrayList<BookletItem> mItems = new ArrayList<>();

    private RelativeLayout booklet_layout;
    private ImageView booklet_img;

    BookletItem current_item;


    public BookletListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public BookletItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.booklet_img, parent, false);
        }

        booklet_layout = (RelativeLayout) convertView.findViewById(R.id.booklet_layout);
        booklet_img = (ImageView) convertView.findViewById(R.id.booklet_img);

        if(current_item != null){
            Glide.with(context).load(current_item.getImg_resource()).into(booklet_img);
        }

        final int img_resource = current_item.getImg_resource();

        booklet_layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("img_resource", img_resource);
            context.startActivity(intent);
        });


        return convertView;
    }

    public void addItem(int img_resource){
        BookletItem mItem = new BookletItem();

        mItem.setImg_resource(img_resource);

        mItems.add(mItem);
    }
}

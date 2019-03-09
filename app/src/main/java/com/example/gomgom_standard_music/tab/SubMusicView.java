package com.example.gomgom_standard_music.tab;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.gomgom_standard_music.R;

public class SubMusicView extends LinearLayout {

  Context context;
  View view;

  public SubMusicView(Context context, int index) {
    super(context);

    this.context=context;
    LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_submusic, this, true);
    initSetting(index);
  }

  public void initSetting(int index){
    ImageView imageView = (ImageView) view.findViewById(R.id.play_2_trackImage);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    switch(index){
      case 0:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.booklet_img_08).into(imageView);
        break;

      case 1:
        Glide.with(getContext()).load(R.drawable.booklet_img_01).into(imageView);
        break;

      case 2:
        Glide.with(getContext()).load(R.drawable.booklet_img_06).into(imageView);
        break;

      case 3:
        Glide.with(getContext()).load(R.drawable.booklet_img_08).into(imageView);
        break;

      case 4:
        Glide.with(getContext()).load(R.drawable.booklet_img_04).into(imageView);
        break;

      case 5:
        Glide.with(getContext()).load(R.drawable.booklet_img_05).into(imageView);
        break;

      case 6:
        Glide.with(getContext()).load(R.drawable.booklet_img_06).into(imageView);
        break;

      case 7:
        Glide.with(getContext()).load(R.drawable.booklet_img_01).into(imageView);
        break;
    }

    //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

  }

}
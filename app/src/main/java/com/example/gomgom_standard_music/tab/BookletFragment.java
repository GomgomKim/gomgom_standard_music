package com.example.gomgom_standard_music.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.BookletListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookletFragment extends Fragment {
    /*@BindView(R.id.v_seekbar_w) VerticalSeekBarWrapper v_seekbar_w;
    @BindView(R.id.v_seekbar) VerticalSeekBar v_seekbar;*/
    @BindView(R.id.title_img) ImageView title_img;
    @BindView(R.id.bottom_img) ImageView bottom_img;
    @BindView(R.id.gallary_list) ListView gallary_list;
    RelativeLayout layout = null;

    BookletListAdapter bookletListAdapter;


    public BookletFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_booklet, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        adapterSetting();
//        seekBarSetting();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.booklet_title).into(title_img);
        Glide.with(getContext()).load(R.drawable.booklet_bottom).into(bottom_img);
        title_img.bringToFront();
    }

    public void adapterSetting(){
        bookletListAdapter = new BookletListAdapter(getContext());
        bookletListAdapter.addItem(R.drawable.booklet_img_02);
        bookletListAdapter.addItem(R.drawable.booklet_img_03);
       /* bookletListAdapter.addItem(R.drawable.booklet_img_01);
        bookletListAdapter.addItem(R.drawable.booklet_img_04);
        bookletListAdapter.addItem(R.drawable.booklet_img_05);
        bookletListAdapter.addItem(R.drawable.booklet_img_06);*/
        gallary_list.setAdapter(bookletListAdapter);
    }

    /*public void seekBarSetting(){
        v_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gallary_list.setSelection(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        v_seekbar.getProgressDrawable().setColorFilter(Color.parseColor("#80ffffff"), PorterDuff.Mode.SRC_IN);
        v_seekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        v_seekbar_w.bringToFront();
        v_seekbar.bringToFront();
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                gallary_list.setSelection(0);
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }


}

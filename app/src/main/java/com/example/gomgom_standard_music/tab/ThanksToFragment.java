package com.example.gomgom_standard_music.tab;


import android.content.res.AssetManager;
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
import com.example.gomgom_standard_music.adapter.ThanksToListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThanksToFragment extends Fragment {

    @BindView(R.id.top_layout) ImageView top_layout;
    @BindView(R.id.thanks_to_list) ListView thanks_to_list;

    RelativeLayout layout = null;

    ThanksToListAdapter thanksToListAdapter;

    public ThanksToFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_thanks_to, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        listSetting();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.thanks_title).into(top_layout);
    }

    public void listSetting(){
        // make content
        AssetManager am = getActivity().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read=null;
        String terms="";
        try {
            inputStream = am.open("to_fan.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read=br.readLine())!=null){
                terms+=read;
                terms+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        thanksToListAdapter = new ThanksToListAdapter(getContext());
        thanksToListAdapter.addItem("FAN", "ZICO", terms);
        thanksToListAdapter.addItem("팬시차일드크루", "지코", terms);

        thanks_to_list.setAdapter(thanksToListAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                thanks_to_list.setSelection(0);
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }

}

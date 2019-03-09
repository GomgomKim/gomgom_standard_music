package com.example.gomgom_standard_music.tab;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.PlayListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    @BindView(R.id.title_bg) ImageView title_bg;
    @BindView(R.id.album_intro) LinearLayout album_intro;

    @BindView(R.id.list_profile_img) ImageView list_profile_img;
    @BindView(R.id.play_list) ListView play_list;

    RelativeLayout layout = null;
    PlayListAdapter playListAdapter;

    // 임시저장
    ArrayList<String> titles;
    ArrayList<Integer> heart_nums;

    //팝업
    View popupView;
    PopupWindow pw;


    public ListFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, layout);

        // 임시저장
        makeData();

        initSetting();
        albumIntroSetting();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.songlist_title).into(title_bg);
        Glide.with(getContext()).load(R.drawable.songlist_album)
                .apply(new RequestOptions().circleCrop())
                .into(list_profile_img);

        playListAdapter = new PlayListAdapter(getContext());
        for(int i=0; i<titles.size(); i++){
            if ( i == 1 )
                playListAdapter.addItem(0, i+1, R.drawable.btn_like_on, heart_nums.get(i), titles.get(i), "지코(ZICO)", 1);
            else
                playListAdapter.addItem(0, i+1, R.drawable.btn_like_off, heart_nums.get(i), titles.get(i), "지코(ZICO)", 0);
        }

        play_list.setAdapter(playListAdapter);
    }

    public void albumIntroSetting(){
        album_intro.setOnClickListener(view -> terms_popup_window(view));
    }

    public void terms_popup_window(View v){

        LayoutInflater inf = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView =inf.inflate(R.layout.album_intro,null);

        pw = new PopupWindow(v);

        pw.setContentView(popupView);
        pw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        // pw.setTouchable(true);
        pw.setFocusable(true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //popup 내용
        ImageButton btn_close = (ImageButton)popupView.findViewById(R.id.btn_close);
        TextView title = (TextView)popupView.findViewById(R.id.title);
        TextView content = (TextView)popupView.findViewById(R.id.content);

        // make title
        AssetManager am = getActivity().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read_title=null;
        String terms_title="";
        try {
            inputStream = am.open("album_title.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read_title=br.readLine())!=null){
                terms_title+=read_title;
                terms_title+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String read_content=null;
        String terms_content="";
        try {
            inputStream = am.open("album_content.txt");
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            br = new BufferedReader(inputStreamReader);

            while((read_content=br.readLine())!=null){
                terms_content+=read_content;
                terms_content+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        title.setText(terms_title);
        content.setText(terms_content);

        btn_close.setOnClickListener(view -> pw.dismiss());

        pw.showAsDropDown(v);
    }


    // 임시저장
    public void makeData(){
        titles = new ArrayList<>();
        heart_nums = new ArrayList<>();
        titles.add("BERMUDA TRIANGLE");      heart_nums.add(13);
        titles.add("She's Baby");      heart_nums.add(1789);
        titles.add("Artist");       heart_nums.add(13542);
        titles.add("FANXY CHILD");   heart_nums.add(486);
        titles.add("천재(Behind the scene)");           heart_nums.add(992);
        titles.add("Okey Dokey");         heart_nums.add(22396);
        titles.add("나는 나 너는 너");    heart_nums.add(9);
        titles.add("SoulMate"); heart_nums.add(75);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){

            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }

}

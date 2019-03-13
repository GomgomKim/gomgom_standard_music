package com.example.gomgom_standard_music.tab;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.CommentListAdapter;
import com.example.gomgom_standard_music.events.GetSongPlayInfoEvent;
import com.example.gomgom_standard_music.item.VideoListItem;
import com.example.gomgom_standard_music.service.BusProvider;
import com.example.gomgom_standard_music.service.MusicService;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    @BindView(R.id.btn_close) ImageButton btn_close;
    @BindView(R.id.video_title) TextView video_title;

    @BindView(R.id.view_count) TextView view_count_view;
    @BindView(R.id.comment_count) TextView comment_count_view;
    @BindView(R.id.like_layout) LinearLayout like_layout;
    @BindView(R.id.like_count) TextView like_count_view;
    @BindView(R.id.like_img) ImageView like_img_view;
    @BindView(R.id.btn_comment) ImageView btn_comment;

    @BindView(R.id.order_by_recent) LinearLayout order_by_recent;
    @BindView(R.id.order_by_like_count) LinearLayout order_by_like_count;
    @BindView(R.id.write_comment) LinearLayout write_comment;
    @BindView(R.id.btn_write) ImageView btn_write;

    @BindView(R.id.comment_list) ListView comment_list;

    @BindView(R.id.recent) TextView recent;
    @BindView(R.id.like) TextView like;


    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener listener;

    public static final String API_KEY = "AIzaSyDi54gnjDssDmXAfG1X-rJs4OmuYjH8iGM";

    private String video_code = "";
    private String video_name = "";
    private int view_count = 0;
    private int like_count = 0;
    private int comment_count = 0;
    private int like_img = 0;

    private boolean isPlaying = false;
    private boolean musicPlayed = false;

    CommentListAdapter commentListAdapter;

    String list_state = "최신순";

    //Data Setting - DB세팅 후 없어짐
    private ArrayList<VideoListItem> mItems = new ArrayList<>();
    private int position = 0;
    ArrayList<String> user_name_arr;
    ArrayList<String> date_arr;
    ArrayList<String> comment_text_arr;
    ArrayList<Integer> like_count_arr;
    ArrayList<Integer> heart_arr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        eventBus();
        getVideoInfo();
        initSetting();
        orderSetting();
        dataSetting(); // DB세팅 후 없어짐
        listSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(musicPlayed){
            Intent intent_service = new Intent(this, MusicService.class);
            intent_service.putExtra("state", "play");
            startService(intent_service);

        }
        BusProvider.getInstance().unregister(this);
    }

    public void eventBus(){
        BusProvider.getInstance().register(this);
    }

    // 초기화
    @Subscribe
    public void FinishLoad(GetSongPlayInfoEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        isPlaying = mEvent.getIsPlay();
        if(isPlaying && !musicPlayed){
            Intent intent_service = new Intent(this, MusicService.class);
            intent_service.putExtra("state", "pause");
            startService(intent_service);
            musicPlayed = true;
        }
    }

    public void getVideoInfo(){
        Intent intent = getIntent();
        mItems = (ArrayList<VideoListItem>) intent.getSerializableExtra("mItems");
        position = intent.getExtras().getInt("position", 0);
        video_code = mItems.get(position).getVideo_code();
        video_name = mItems.get(position).getTitle();
        view_count = mItems.get(position).getView_count();
        like_count = mItems.get(position).getLike_count();
        comment_count = mItems.get(position).getComment_count();
        like_img = mItems.get(position).getHeart();
    }

    public void initSetting(){
        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.youtube);
        video_title.setText(video_name);
        view_count_view.setText(String.valueOf(view_count));
        like_count_view.setText(String.valueOf(like_count));
        comment_count_view.setText(String.valueOf(comment_count));
        Glide.with(this).load(like_img)
                .apply(new RequestOptions().fitCenter()).into(like_img_view);
        Glide.with(this).load(R.drawable.btn_comment)
                .apply(new RequestOptions().fitCenter()).into(btn_comment);
        Glide.with(this).load(R.drawable.btn_write)
                .apply(new RequestOptions().fitCenter()).into(btn_write);

        SpannableString content = new SpannableString("최신순");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        recent.setText(content);

        like_layout.setOnClickListener(view -> {
            int is_heart = mItems.get(position).getHeart();
            if(is_heart == R.drawable.btn_like_off){
                mItems.get(position).setHeart(R.drawable.btn_like_on);
                mItems.get(position).setLike_count(mItems.get(position).getLike_count()+1);
            } else if (is_heart == R.drawable.btn_like_on) {
                mItems.get(position).setHeart(R.drawable.btn_like_off);
                mItems.get(position).setLike_count(mItems.get(position).getLike_count()-1);
            }
            like_count_view.setText(String.valueOf(mItems.get(position).getLike_count()));
            Glide.with(this).load(mItems.get(position).getHeart())
                    .apply(new RequestOptions().fitCenter()).into(like_img_view);
        });

        //리스너 등록부분
        listener = new YouTubePlayer.OnInitializedListener(){

            //초기화 성공시
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(video_code); //url의 맨 뒷부분 ID값만 넣으면 됨
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        };

        youTubePlayerView.initialize(API_KEY,this);

        btn_close.setOnClickListener(v -> {
            finish();
        });

        btn_close.bringToFront();

    }

    public void orderSetting(){
        order_by_recent.setOnClickListener(view -> {
            list_state = "최신순";
            SpannableString content = new SpannableString("최신순");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            recent.setText(content);
            like.setText("추천순");
            dataSetting();
            listSetting();
            commentListAdapter.notifyDataSetChanged();
        });

        order_by_like_count.setOnClickListener(view -> {
             list_state = "추천순";
             SpannableString content = new SpannableString("추천순");
             content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
             like.setText(content);
             recent.setText("최신순");
             dataSetting();
             listSetting();
             commentListAdapter.notifyDataSetChanged();
        });
    }

    public void listSetting(){
        commentListAdapter = new CommentListAdapter(getApplicationContext());
        for(int i = 0; i<user_name_arr.size(); i++){
            if(list_state.equals("최신순")) {
                if(i == 2){
                    commentListAdapter.addItem(user_name_arr.get(i), date_arr.get(i), comment_text_arr.get(i), String.valueOf(R.drawable.booklet_img_03), heart_arr.get(i), like_count_arr.get(i));
                    continue;
                }
            } else if (list_state.equals("추천순")){
                if(i == 1){
                    commentListAdapter.addItem(user_name_arr.get(i), date_arr.get(i), comment_text_arr.get(i), String.valueOf(R.drawable.booklet_img_03), heart_arr.get(i), like_count_arr.get(i));
                    continue;
                }
            }

            commentListAdapter.addItem(user_name_arr.get(i), date_arr.get(i), comment_text_arr.get(i), "", heart_arr.get(i), like_count_arr.get(i));
        }

        comment_list.setAdapter(commentListAdapter);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(null== youTubePlayer) return;

        //Start buffering
        if (!b) {
            //youTubePlayer.cueVideo(video_code); // 자동재생 X
            youTubePlayer.loadVideo(video_code); // 자동재생 O
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();


    }

    public void dataSetting(){
        user_name_arr = new ArrayList<>();
        date_arr = new ArrayList<>();
        comment_text_arr = new ArrayList<>();
        heart_arr = new ArrayList<>();
        like_count_arr = new ArrayList<>();

        if(list_state.equals("최신순")){
            user_name_arr.add("기연킴"); date_arr.add("2018.09.23"); comment_text_arr.add("얼른 나아서 지코공연 보러가야지 ~~");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(6742);

            user_name_arr.add("언어의마술사"); date_arr.add("2018.11.25"); comment_text_arr.add("발렌타인데이는 역시 솔초코");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(11);

            user_name_arr.add("romantic"); date_arr.add("2019.01.13"); comment_text_arr.add("지아코 ~!~! 넘모좋아 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(353);

            user_name_arr.add("gomgomKim"); date_arr.add("2019.02.21"); comment_text_arr.add("이번 스탠다드 앱은 지아코로 만들어서 얼마나 좋게요 ~! 두줄 이상 적으면 어떻게 되는지 확인하면 얼마나 좋게요 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(9);

        } else if(list_state.equals("추천순")){
            user_name_arr.add("기연킴"); date_arr.add("2018.09.23"); comment_text_arr.add("얼른 나아서 지코공연 보러가야지 ~~");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(6742);

            user_name_arr.add("romantic"); date_arr.add("2019.01.13"); comment_text_arr.add("지아코 ~!~! 넘모좋아 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(353);

            user_name_arr.add("언어의마술사"); date_arr.add("2018.11.25"); comment_text_arr.add("발렌타인데이는 역시 솔초코");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(11);

            user_name_arr.add("gomgomKim"); date_arr.add("2019.02.21"); comment_text_arr.add("이번 스탠다드 앱은 지아코로 만들어서 얼마나 좋게요 ~! 두줄 이상 적으면 어떻게 되는지 확인하면 얼마나 좋게요 ~!");
            heart_arr.add(R.drawable.btn_like_off); like_count_arr.add(9);
        }
    }
}

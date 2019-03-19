package com.example.gomgom_standard_music.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.gomgom_standard_music.R;
import com.example.gomgom_standard_music.adapter.LeftCover;
import com.example.gomgom_standard_music.adapter.ViewPagerAdapter;
import com.example.gomgom_standard_music.events.EndFragEvent;
import com.example.gomgom_standard_music.interfaces.MainInterface;
import com.example.gomgom_standard_music.service.BusProvider;
import com.example.gomgom_standard_music.tab.MiniPlayerFragment;
import com.example.gomgom_standard_music.tab.MusicFragment;
import com.example.gomgom_standard_music.tab.ReviewFragment;
import com.example.gomgom_standard_music.tab.ScheduleFragment;
import com.example.gomgom_standard_music.tab.SnsFragment;
import com.example.gomgom_standard_music.tab.SurviceInfoActivity;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity implements MainInterface {

    private LeftCover leftCover;
    private ActionBarDrawerToggle dtToggle;
    private FrameLayout frameLayout;
    private FrameLayout miniplayer_layout;

    ViewPager mainPager;
    ViewPagerAdapter viewPagerAdapter;
    SetViewPagerTabListener setViewPagerTabListener;
    public static int position=0;
    public static int state=0;

    private boolean fragment_state = false;

    //tool bar
    Toolbar toolbar;
    DrawerLayout dlDrawer;

    public interface SetViewPagerTabListener{
        void setTab(int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetting();
        setToolbar();
    }

    public void initSetting(){
        BusProvider.getInstance().register(this);

        mainPager=(ViewPager)findViewById(R.id.mainPager);
        mainPager.setOffscreenPageLimit(6);
        setViewPagerTabListener= position -> {
            switch (position){
                case 0:
                    mainPager.setCurrentItem(0);
                    break;
                case 1:
                    mainPager.setCurrentItem(1);
                    break;
                case 2:
                    mainPager.setCurrentItem(2);
                    break;
                case 3:
                    mainPager.setCurrentItem(3);
                    break;
                case 4:
                    mainPager.setCurrentItem(4);
                    break;
                case 5:
                    mainPager.setCurrentItem(5);
                    break;
            }
        };

        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), setViewPagerTabListener);
        mainPager.setAdapter(viewPagerAdapter);

        frameLayout = (FrameLayout)findViewById(R.id.fragment_container);
        miniplayer_layout = (FrameLayout)findViewById(R.id.miniplayer_layout);
        miniplayer_layout.setVisibility(View.GONE);
        miniplayer_layout.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MusicFragment())
                    .commit();
            toolbar.setVisibility(View.GONE);
            fragment_state = true;

            miniplayer_layout.setVisibility(View.GONE);
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.miniplayer_layout, new MiniPlayerFragment())
                .commit();
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlDrawer.addDrawerListener(dtToggle);
        dtToggle.syncState();

        Drawable drawable = getResources().getDrawable(R.drawable.left_mn_side);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 80, 80, true));
        toolbar.setNavigationIcon(newdrawable);

        leftCover = (LeftCover) getSupportFragmentManager().findFragmentById(R.id.drawer);

        ImageButton buttonCloseDrawer = (ImageButton) findViewById(R.id.btn_close);
        buttonCloseDrawer.setOnClickListener(arg0 -> dlDrawer.closeDrawers());

        toolbar.bringToFront();

        LinearLayout nav_cover = (LinearLayout) findViewById(R.id.nav_cover);
        nav_cover.setOnClickListener(view -> {
            fragmentReset();
            setViewPagerTabListener.setTab(0);
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
        });

        LinearLayout nav_play_list = (LinearLayout) findViewById(R.id.nav_play_list);
        nav_play_list.setOnClickListener(view -> {
            fragmentReset();
            setViewPagerTabListener.setTab(1);
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
        });

        LinearLayout nav_booklet = (LinearLayout) findViewById(R.id.nav_booklet);
        nav_booklet.setOnClickListener(view -> {
            fragmentReset();
            setViewPagerTabListener.setTab(2);
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
        });

        LinearLayout nav_video = (LinearLayout) findViewById(R.id.nav_video);
        nav_video.setOnClickListener(view -> {
            fragmentReset();
            setViewPagerTabListener.setTab(3);
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
        });

        LinearLayout nav_thanks_to = (LinearLayout) findViewById(R.id.nav_thanks_to);
        nav_thanks_to.setOnClickListener(view -> {
            fragmentReset();
            setViewPagerTabListener.setTab(4);
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
        });

        LinearLayout logo_layout = (LinearLayout) findViewById(R.id.logo_layout);
        logo_layout.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SurviceInfoActivity.class)));

        ImageButton nav_schedule = (ImageButton) findViewById(R.id.nav_schedule);
        nav_schedule.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ScheduleFragment())
                    .commit();
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
            fragment_state = true;
        });

        ImageButton nav_sns = (ImageButton) findViewById(R.id.nav_sns);
        nav_sns.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SnsFragment())
                    .commit();
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
            fragment_state = true;
        });

        ImageButton nav_review = (ImageButton) findViewById(R.id.nav_review);
        nav_review.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ReviewFragment())
                    .commit();
            toolbar.bringToFront();
            dlDrawer.closeDrawers();
            fragment_state = true;
        });
    }

    @Override
    public void showMusicPlayer() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MusicFragment())
                .commit();
        fragment_state = true;
        toolbar.setVisibility(View.GONE);

        miniplayer_layout.setVisibility(View.GONE);
    }


    @Override
    public void showMiniPlayer() {
        miniplayer_layout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeFragment() {
        fragmentReset();
    }

    @Override
    public void setCoverLayout() {
        RelativeLayout.LayoutParams topButtonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mainPager.setLayoutParams(topButtonParams);
    }

    public void fragmentReset(){
        if(fragment_state){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                    .commit();
            fragment_state=false;
        }
    }


    public static void setPosition(int p){
        position = p;
    }

    public static int getPosition(){
        return position;
    }

    public static void setState(int s){
        state = s;
    }

    public static int getState(){
        return state;
    }

    @Subscribe
    public void FinishLoad(EndFragEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        boolean end_frag = mEvent.isEnd_frag();
        if(end_frag == true) {
            miniplayer_layout.setVisibility(View.VISIBLE);
        } else{

        }
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
}


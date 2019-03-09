package com.example.gomgom_standard_music.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.gomgom_standard_music.main.MainActivity;
import com.example.gomgom_standard_music.tab.BookletFragment;
import com.example.gomgom_standard_music.tab.CoverFragment;
import com.example.gomgom_standard_music.tab.ListFragment;
import com.example.gomgom_standard_music.tab.ThanksToFragment;
import com.example.gomgom_standard_music.tab.VideoFragment;


//Pager Adapter
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public static MainActivity.SetViewPagerTabListener setViewPagerTabListener;
    public ViewPagerAdapter(FragmentManager fragmentManager, MainActivity.SetViewPagerTabListener setViewPagerTabListener){
        super(fragmentManager);
        this.setViewPagerTabListener=setViewPagerTabListener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch(position){
            case 0:
                return new CoverFragment();
            case 1:
                return new ListFragment();
            case 2:
                return new BookletFragment();
            case 3:
                return new VideoFragment();
            case 4:
                return new ThanksToFragment();
            default:
                return null;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 5;
    }


}
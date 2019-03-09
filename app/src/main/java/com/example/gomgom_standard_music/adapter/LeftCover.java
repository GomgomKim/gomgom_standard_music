package com.example.gomgom_standard_music.adapter;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gomgom_standard_music.R;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeftCover extends Fragment {

    NavigationView layout = null;

    public LeftCover() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (NavigationView) inflater.inflate(R.layout.fragment_left_cover, container, false);
        ButterKnife.bind(this, layout);

        return layout;
    }


}

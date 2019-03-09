package com.example.gomgom_standard_music.events;

public class IsPlayEvent {
    public boolean is_play;

    public IsPlayEvent(boolean is_play){
        this.is_play = is_play;
    }

    public boolean isPlaying(){
        return is_play;
    }

}

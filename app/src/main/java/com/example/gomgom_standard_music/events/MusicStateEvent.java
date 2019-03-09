package com.example.gomgom_standard_music.events;

public class MusicStateEvent {
    public int duration;

    public MusicStateEvent(int duration){
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }

}

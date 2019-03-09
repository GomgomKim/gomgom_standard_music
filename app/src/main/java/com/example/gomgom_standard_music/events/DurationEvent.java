package com.example.gomgom_standard_music.events;

public class DurationEvent {
    public int duration;

    public DurationEvent(int duration){
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }

}

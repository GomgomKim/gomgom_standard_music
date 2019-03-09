package com.example.gomgom_standard_music.events;

public class TimerEvent {
    public int time;

    public TimerEvent(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }

}

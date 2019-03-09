package com.example.gomgom_standard_music.events;

public class SeekbarEvent {
    public int seek_position;

    public SeekbarEvent(int seek_position){
        this.seek_position = seek_position;
    }

    public int getSeekPosition(){
        return seek_position;
    }

}

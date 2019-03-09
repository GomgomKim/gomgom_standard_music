package com.example.gomgom_standard_music.events;

public class EndFragEvent {
    public boolean end_frag;

    public EndFragEvent(boolean end_frag){
        this.end_frag = end_frag;
    }

    public boolean isEnd_frag(){
        return end_frag;
    }

}

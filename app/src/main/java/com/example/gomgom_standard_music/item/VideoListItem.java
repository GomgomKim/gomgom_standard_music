package com.example.gomgom_standard_music.item;

import java.io.Serializable;

public class VideoListItem implements Serializable {
    private String title;
    private String video_code;
    private int view_count;
    private int like_count;
    private int heart;
    private int comment_count;

    public VideoListItem(){}

    public void setTitle(String title){ this.title = title; }
    public String getTitle() { return this.title; }

    public void setVideo_code(String video_code){ this.video_code = video_code; }
    public String getVideo_code() { return this.video_code; }

    public void setView_count(int view_count){ this.view_count = view_count; }
    public int getView_count() { return this.view_count; }

    public void setLike_count(int like_count){ this.like_count = like_count; }
    public int getLike_count() { return this.like_count; }

    public void setHeart(int heart){ this.heart = heart; }
    public int getHeart() { return this.heart; }

    public void setComment_count(int comment_count){ this.comment_count = comment_count; }
    public int getComment_count() { return this.comment_count; }
}

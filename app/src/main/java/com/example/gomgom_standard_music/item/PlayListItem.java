package com.example.gomgom_standard_music.item;

public class PlayListItem {
    private int type = 0; // 곡 : 0 , 앨범 소개 : 1

    private String index = "";
    private int heart;
    private int heart_num = 0;
    private String title = "";
    private String singer = "";
    private int isTitle = 0;

    public PlayListItem(){}

    public void setType(int type){ this.type = type; }
    public int getType() { return this.type; }

    public void setIndex(String index){ this.index = index; }
    public String getIndex() { return this.index; }

    public void setHeart(int heart){ this.heart = heart; }
    public int getHeart() { return this.heart; }

    public void setHeart_num(int heart_num){ this.heart_num = heart_num; }
    public int getHeart_num() { return this.heart_num; }

    public void setTitle(String title){ this.title = title; }
    public String getTitle() { return this.title; }

    public void setSinger(String singer){ this.singer = singer; }
    public String getSinger() { return this.singer; }

    public void setIsTitle(int isTitle){ this.isTitle = isTitle; }
    public int getIsTitle() { return this.isTitle; }

}

package com.example.gomgom_standard_music.item;

public class CommentListItem {
    private String user_name = "";
    private String date = "";
    private String comment_img = "";
    private String comment_text = "";
    private int like_count = 0;
    private int heart;

    public CommentListItem(){}

    public void setUser_name(String user_name){ this.user_name = user_name; }
    public String getUser_name() { return this.user_name; }

    public void setDate(String date){ this.date = date; }
    public String getDate() { return this.date; }

    public void setComment_img(String comment_img){ this.comment_img = comment_img; }
    public String getComment_img() { return this.comment_img; }

    public void setComment_text(String comment_text){ this.comment_text = comment_text; }
    public String getComment_text() { return this.comment_text; }

    public void setLike_count(int like_count){ this.like_count = like_count; }
    public int getLike_count() { return this.like_count; }

    public void setHeart(int heart){ this.heart = heart; }
    public int getHeart() { return this.heart; }

}

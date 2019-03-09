package com.example.gomgom_standard_music.item;

public class ThanksToItem {
    private String to_who;
    private String by_who;
    private String text;
    private String text_backup;
    private int is_long=0;

    public ThanksToItem(){}

    public void setTo_who(String to_who){ this.to_who = to_who; }
    public String getTo_who() { return this.to_who; }

    public void setBy_who(String by_who){ this.by_who = by_who; }
    public String getBy_who() { return this.by_who; }

    public void setText(String text){ this.text = text; }
    public String getText() { return this.text; }

    public void setText_backup(String text_backup){ this.text_backup = text_backup; }
    public String getText_backup() { return this.text_backup; }

    public void setIs_long(int is_long){ this.is_long = is_long; }
    public int getIs_long() { return this.is_long; }

}

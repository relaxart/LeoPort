package com.lingualeo.client;

public class Translate {
    public int votes;
    public int id;
    public String picUrl;
    public String value;
    public boolean isUser;

    public Translate(int votes, int id, String picUrl, String value, boolean isUser) {
        this.votes = votes;
        this.id = id;
        this.picUrl = picUrl;
        this.value = value;
        this.isUser = isUser;
    }
}

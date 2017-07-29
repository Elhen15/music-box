package com.finalproject.elhen15.musicbox.Model;

import java.util.Date;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public class MusicPost extends Entity {
    private String title;
    private String desc;
    private String imageUrl;
    private User user;
    private boolean isDeleted;

    public MusicPost(String id,String title, String imageUrl, String desc){
        super(id);
        this.setTitle(title);
        this.setDesc(desc);
        this.setDeleted(false);
        this.setImageUrl(imageUrl);
    }

    public MusicPost(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

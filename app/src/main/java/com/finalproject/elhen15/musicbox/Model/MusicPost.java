package com.finalproject.elhen15.musicbox.Model;

import java.util.Date;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public class MusicPost {
    public String id;
    public String title;
    public String desc;
    public String dateCreated;
    public String imageUrl;
    public String user_id;
    public boolean isDeleted;

    public MusicPost(String title, String imageUrl, String desc){
        this.dateCreated = new Date().toString();
        this.title = title;
        this.desc = desc;
        this.isDeleted = false;
        this.imageUrl = imageUrl;
    }

    public MusicPost(){

    }



}

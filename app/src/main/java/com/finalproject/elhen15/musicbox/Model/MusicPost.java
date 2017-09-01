package com.finalproject.elhen15.musicbox.Model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public class MusicPost extends Entity implements Comparable<MusicPost> {
    private String title;
    private String desc;
    private String imageUrl;
    private User user;
    private ArrayList <Comment> comments;
    private boolean isDeleted;
    private int likesCount;
    private String youtubeURL;

    public MusicPost(String title, String imageUrl, String desc, String youtubeURL){
        super();
        this.setTitle(title);
        this.setDesc(desc);
        this.setDeleted(false);
        this.setImageUrl(imageUrl);
        this.setLikesCount(0);
        this.setYoutubeURL(youtubeURL);
    }

    public MusicPost(){
        super();
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
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

    public void setIsDeleted(boolean isDeleted){
        this.isDeleted=isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    @Override
    public int compareTo(MusicPost musicPost) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        //this optimization is usually worthwhile, and can
        //always be added
        if (this == musicPost) return EQUAL;

        //primitive numbers follow this form
        if (this.getLikesCount() < musicPost.getLikesCount()) return AFTER;
        if (musicPost.getLikesCount() > musicPost.getLikesCount()) return BEFORE;

        return EQUAL;
    }
}

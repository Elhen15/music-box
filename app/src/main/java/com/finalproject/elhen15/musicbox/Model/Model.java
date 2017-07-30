package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Elhen15 on 29/07/2017.
 */

import java.util.ArrayList;
import java.util.Calendar;


public class Model {
    public final static Model instance = new Model();
    private Model(){
        User user = new User("Elhen15@Gmail.com","222",true);

        for(int i = 0; i < 5; i++) {
            MusicPost musicPost= new MusicPost();
            musicPost.setTitle("Metallica " + i);
            musicPost.setDesc("bla bla bla vvivjfi i jifjif ifjijf ijfijfi \n fjijf ijfijf ijifj iji "+ i);
            musicPost.setUser(user);
            data.add(i, musicPost);
        }
    }

    private ArrayList<MusicPost> data = new ArrayList<>();

    public void addPost(MusicPost musicPost){
        musicPost.setImageUrl("../res/drawable/grid.png");
        data.add(musicPost.getId(), musicPost);
    }

    public ArrayList<MusicPost> getAllMusicPosts(){
        return data;
    }

    public MusicPost getPostByID (int postID){
        for (MusicPost musicPost: data) {
            if (musicPost.getId() == postID)
                return musicPost;
        }

        return null;
    }

    public Boolean removePost(MusicPost musicPost) {
        return data.remove(musicPost);
    }

    public Boolean editPost(MusicPost musicPost){
        if (this.getPostByID(musicPost.getId()) == null) {
            this.addPost(musicPost);
        }else {
            data.set(musicPost.getId(), musicPost);
        }

        return true;
    }
}
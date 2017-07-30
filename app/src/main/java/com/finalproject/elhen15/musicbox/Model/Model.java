package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Elhen15 on 29/07/2017.
 */

import java.util.ArrayList;
import java.util.Calendar;


public class Model {
    public final static Model instance = new Model();
    private static int id = 1;
    private Model(){
        User user = new User("Elhen15@Gmail.com","222",true,"321");

        for(int i = 0; i < 5; i++) {
            MusicPost musicPost= new MusicPost();
            musicPost.setTitle("Metallica " + i);
            musicPost.setDesc("bla bla bla vvivjfi i jifjif ifjijf ijfijfi \n fjijf ijfijf ijifj iji "+ i);
            musicPost.setId(id + "");
            musicPost.setUser(user);
            data.add(i, musicPost);
            id++;
        }
    }

    private ArrayList<MusicPost> data = new ArrayList<>();

    public void addPost(MusicPost musicPost){
        musicPost.setId(++id + "");
        musicPost.setImageUrl("../res/drawable/grid.png");
        data.add(Integer.parseInt(musicPost.getId()), musicPost);
    }

    public ArrayList<MusicPost> getAllMusicPosts(){
        return data;
    }

    public MusicPost getPostByID (String postID){
        for (MusicPost musicPost: data) {
            if (musicPost.getId().equals(postID))
                return musicPost;
        }

        return null;
    }

    public Boolean removePost(MusicPost musicPost) {
        id--;
        return data.remove(musicPost);
    }

    public Boolean editPost(MusicPost musicPost){
        if (this.getPostByID(musicPost.getId()) == null) {
            this.addPost(musicPost);
        }else {
            data.set(Integer.parseInt(musicPost.getId()), musicPost);
        }

        return true;
    }
}
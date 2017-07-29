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
        Calendar c = Calendar.getInstance();

        for(int i = 0; i < 5; i++) {
            MusicPost musicPost= new MusicPost();
            musicPost.title = "Metallica " + i;
            musicPost.id = id + "";
            musicPost.dateCreated = c.getTime().toString();
            data.add(i, musicPost);
            id++;
        }
    }

    private ArrayList<MusicPost> data = new ArrayList<>();

    public void addPost(MusicPost musicPost){
        musicPost.id = ++id + "";
        musicPost.imageUrl = "../res/drawable/grid.png";
        data.add(Integer.parseInt(musicPost.id), musicPost);
    }

    public ArrayList<MusicPost> getAllMusicPosts(){
        return data;
    }

    public MusicPost getPostByID (String postID){
        for (MusicPost musicPost: data) {
            if (musicPost.id.equals(postID))
                return musicPost;
        }

        return null;
    }

    public Boolean removePost(MusicPost musicPost) {
        id--;
        return data.remove(musicPost);
    }

    public Boolean editPost(MusicPost musicPost){
        if (this.getPostByID(musicPost.id) == null) {
            this.addPost(musicPost);
        }else {
            data.set(Integer.parseInt(musicPost.id), musicPost);
        }

        return true;
    }
}
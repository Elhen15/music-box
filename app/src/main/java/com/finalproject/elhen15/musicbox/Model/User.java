package com.finalproject.elhen15.musicbox.Model;

import java.util.ArrayList;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public class User extends Entity {
    private String email;
    private String password;
    private boolean isAdmin;
    private ArrayList<MusicPost> postLikes;

    public User(){
        super();
        this.postLikes = new ArrayList<MusicPost>();
    }

    public ArrayList<MusicPost> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(ArrayList<MusicPost> postLikes) {
        this.postLikes = new ArrayList<MusicPost>();
        this.postLikes = postLikes;
    }

    public void addPostLike(MusicPost musicPost)
    {
        if (this.postLikes == null)
        {
            this.postLikes = new ArrayList<MusicPost>();
        }
        this.postLikes.add(musicPost);
    }

    public User(String email, String password, boolean isAdmin){
        super();
        this.setPostLikes(new ArrayList<MusicPost>());
        this.setEmail(email);
        this.setPassword(password);
        this.setIsAdmin(isAdmin);

    }

    public User(User copy)
    {
        super();
        this.setPostLikes(copy.getPostLikes());
        this.setEmail(copy.getEmail());
        this.setPassword(copy.getPassword());
        this.setIsAdmin(copy.getIsAdmin());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public boolean getIsAdmin(){return isAdmin;}
}

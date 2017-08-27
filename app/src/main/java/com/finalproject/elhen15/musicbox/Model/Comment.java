package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public class Comment extends Entity {

    private String comment;
    private User user;

    public Comment(){}

    public Comment(String id, String comment, User user) {
        super(id);
        this.comment = comment;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.finalproject.elhen15.musicbox.Model;

import java.util.Date;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public abstract class Entity {
    private String id;
    private Date date;

    public Entity(){

    }

    public Entity(String id)
    {
        this.setDate(new Date());
        this.setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

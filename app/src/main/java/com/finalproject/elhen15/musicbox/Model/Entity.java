package com.finalproject.elhen15.musicbox.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Elhen15 on 29/07/2017.
 */

public abstract class Entity {
    private String id;
    private String date;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Entity(){
        Calendar cal = Calendar.getInstance();
        this.setDate(dateFormat.format(cal.getTime()));
    }

    public Entity(String id)
    {
        Calendar cal = Calendar.getInstance();
        this.setDate(dateFormat.format(cal.getTime()));
        this.setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

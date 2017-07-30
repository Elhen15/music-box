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
    private static int id = 0;
    private String date;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Entity(){
        this.setId(id++);
        Calendar cal = Calendar.getInstance();
        this.setDate(dateFormat.format(cal.getTime()));
    }

    public Entity(int id)
    {
        Calendar cal = Calendar.getInstance();
        this.setDate(dateFormat.format(cal.getTime()));
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

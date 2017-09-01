package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Sharon on 01/09/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ModelSQL extends SQLiteOpenHelper {
    ModelSQL(Context context) {
        super(context, "database.db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PostSQL.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PostSQL.onUpgrade(db, oldVersion, newVersion);

    }
}
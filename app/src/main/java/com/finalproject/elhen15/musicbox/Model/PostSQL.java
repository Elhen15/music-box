package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Sharon on 01/09/2017.
 */

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.finalproject.elhen15.musicbox.MainActivity;

import java.util.LinkedList;
import java.util.List;

public class PostSQL {
    static final String POST_TABLE = "posts";
    static final String POST_ID = "postId";
    static final String POST_TITLE = "title";
    static final String POST_IMAGE_URL = "imageURL";
    static final String POST_DESCRIPTION = "description";
    static final String POST_USER = "userId";
    static final String POST_LIKES = "likes";

    static List<MusicPost> getAllMusicPosts(SQLiteDatabase db) {
        Cursor cursor = db.query(POST_TABLE, null, null, null, null, null, null);
        List<MusicPost> list = new LinkedList<MusicPost>();
        if (cursor.moveToFirst()) {
            do {
                list.add(createMusicPostFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addMusicPost(SQLiteDatabase db, MusicPost post) {
        ContentValues values = new ContentValues();

        values.put(POST_ID, post.getId());
        values.put(POST_TITLE, post.getTitle());
        values.put(POST_IMAGE_URL, post.getImageUrl());
        values.put(POST_DESCRIPTION, post.getDesc());
        values.put(POST_USER, post.getUser().getId());
        values.put(POST_LIKES, post.getLikesCount());

        db.insert(POST_TABLE, POST_ID, values);

    }

    static protected void deleteMusicPost(SQLiteDatabase db, String postId) {
        db.delete(POST_TABLE, POST_ID + "=" + postId, null);
    }

    static MusicPost getPost(SQLiteDatabase db, String postId) {
        String[] selectionArgs = {postId};
        Cursor cursor = db.query(POST_TABLE, null, POST_ID + " = ?", selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            return createMusicPostFromCursor(cursor);
        }

        return null;
    }

    static void editMusicPost(SQLiteDatabase db, MusicPost post){
        ContentValues values = new ContentValues();

        values.put(POST_ID, post.getId());
        values.put(POST_TITLE, post.getTitle());
        values.put(POST_IMAGE_URL, post.getImageUrl());
        values.put(POST_DESCRIPTION, post.getDesc());
        values.put(POST_USER, post.getUser().getId());
        values.put(POST_LIKES, post.getLikesCount());

        db.update(POST_TABLE, values, POST_ID + "=" + post.getId(), null);
    }

    static boolean checkIfIdAlreadyExists(SQLiteDatabase db, String postId) {

        String query = "Select * from " + POST_TABLE + " where " + POST_ID + " = " + postId;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * onCreate is created once we still don't have tables in our DB.
     * We use method to initially create the DB.
     * It was originally from the SQLiteOpenHelper class, although we don't override it.
     */
    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + POST_TABLE +
                " (" +
                POST_ID + " TEXT PRIMARY KEY , " +
                POST_TITLE + " TEXT, " +
                POST_IMAGE_URL + " TEXT, " +
                POST_DESCRIPTION + " TEXT, " +
                POST_LIKES + " TEXT, " +
                POST_USER + " TEXT)");
    }

    /**
     * In onUpgrade scenario we'll just delete the recreate the DB.
     * It was originally from the SQLiteOpenHelper class, although we don't override it.
     */
    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + POST_TABLE);
        onCreate(db);
    }

    @NonNull
    private static MusicPost createMusicPostFromCursor(Cursor cursor) {
        MusicPost post = new MusicPost();

        post.setId(cursor.getString(cursor.getColumnIndex(POST_ID)));
        post.setTitle(cursor.getString(cursor.getColumnIndex(POST_TITLE)));
        post.setImageUrl(cursor.getString(cursor.getColumnIndex(POST_IMAGE_URL)));
        post.setDesc(cursor.getString(cursor.getColumnIndex(POST_DESCRIPTION)));
        post.setLikesCount(cursor.getInt(cursor.getColumnIndex(POST_LIKES)));
        post.setUser(UserSQL.getUser(new ModelSQL(MainActivity.getContext()).getWritableDatabase(), cursor.getString(cursor.getColumnIndex(POST_USER))));

        return post;
    }
}
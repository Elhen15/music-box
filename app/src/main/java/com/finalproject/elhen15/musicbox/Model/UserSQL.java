package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Sharon on 01/09/2017.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public class UserSQL {
    static final String USER_TABLE = "users";
    static final String USER_ID = "userId";
    static final String USER_EMAIL = "email";
    static final String USER_PASSWORD = "password";
    static final String USER_ADMIN = "isAdmin";

    static List<User> getAllUsers(SQLiteDatabase db) {
        Cursor cursor = db.query(USER_TABLE, null, null, null, null, null, null);
        List<User> list = new LinkedList<User>();
        if (cursor.moveToFirst()) {
            do {
                list.add(createUserFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addUser(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();

        values.put(USER_ID, user.getId());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_ADMIN, user.getIsAdmin());

        db.insert(USER_TABLE, USER_ID, values);

    }

    static protected void deleteUser(SQLiteDatabase db, String userId) {
        db.delete(USER_TABLE, USER_ID + "=" + userId, null);
    }

    static User getUser(SQLiteDatabase db, String userId) {
        String[] selectionArgs = {userId};
        Cursor cursor = db.query(USER_TABLE, null, USER_ID + " = ?", selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            return createUserFromCursor(cursor);
        }

        return null;
    }

    static void editUser(SQLiteDatabase db, User user){
        ContentValues values = new ContentValues();

        values.put(USER_ID, user.getId());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_ADMIN, user.getIsAdmin());

        db.update(USER_TABLE, values, USER_ID + "=" + user.getId(), null);
    }

    static boolean checkIfIdAlreadyExists(SQLiteDatabase db, String userId) {

        String query = "Select * from " + USER_TABLE + " where " + USER_ID + " = " + userId;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE +
                " (" +
                USER_ID + " TEXT PRIMARY KEY , " +
                USER_EMAIL + " TEXT, " +
                USER_PASSWORD + " TEXT, " +
                USER_ADMIN + " TEXT, ");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + USER_TABLE);
        onCreate(db);
    }

    @NonNull
    private static User createUserFromCursor(Cursor cursor) {
        User user = new User();

        user.setId(cursor.getString(cursor.getColumnIndex(USER_ID)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(USER_PASSWORD)));
//        user.setIsAdmin(cursor.getInt(cursor.getColumnIndex(USER_ADMIN)));

        return user;
    }
}
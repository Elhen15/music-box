package com.finalproject.elhen15.musicbox.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Elhen15 on 29/08/2017.
 */

public class ModelPostFirebase {

    private static final String POSTS_ID = "PostsId";
    private static final String POSTS_KEY = "Posts";
    private FirebaseDatabase database;
    private DatabaseReference postsReference;

    interface IGetPostId {
        void getId(int id);
    }
    public void getPostId(final IGetPostId callback) {
        DatabaseReference reference = database.getReference(POSTS_ID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int id = dataSnapshot.getValue(int.class);
                callback.getId(id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ModelPostFirebase() {
        database = FirebaseDatabase.getInstance();
        postsReference = database.getReference(POSTS_KEY);
    }

    public void addPost(final MusicPost musicPost){
        getPostId(new IGetPostId() {
            @Override
            public void getId(int id) {
                musicPost.setId(id+"");
                id++;
                setPostsId(id);
                postsReference.child(musicPost.getId()).setValue(musicPost);
            }
        });
    }

    public void setPostsId(int id) {
        DatabaseReference reference = database.getReference(POSTS_ID);
        reference.setValue(id);
    }


}

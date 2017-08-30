package com.finalproject.elhen15.musicbox.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Elhen15 on 29/08/2017.
 */

public class ModelPostFirebase {

    private static final String COMMENTS = "Comments";
    private static final String POSTS_ID = "PostsId";
    private static final String POSTS_KEY = "Posts";
    private FirebaseDatabase database;
    private DatabaseReference postsReference;

    // Firebase
    interface IGetCommentsOfPostCallback{
        void onComplete(ArrayList<Comment> comments);
        void onCancel();
    }

    public void getCommentsOfPosts(String postId,final IGetCommentsOfPostCallback callback) {
        postsReference.child(postId).child(COMMENTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Comment> comments = new ArrayList<>();

                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    comments.add(comment);
                }
                callback.onComplete(comments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    interface IRemovePostCallback{
        void  onComplete(boolean isSuccess);
    }
    public void removePost(MusicPost musicPost, final IRemovePostCallback callback)
    {
        postsReference.child(musicPost.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                callback.onComplete(databaseError == null);
            }
        });

    }


    // works with firebase
    interface IUpdatePostCallback {
        void onComplete(boolean success);
    }
    public void editPost(MusicPost musicPost, final IUpdatePostCallback callback){
        postsReference.child(musicPost.id).setValue(musicPost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                callback.onComplete(databaseError == null);
            }
        });
    }

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


    interface IGetAlPostsCallback {
        void onComplete(ArrayList<MusicPost> posts);
        void onCancel();
    }
    public void getAllPosts(final IGetAlPostsCallback callback){
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MusicPost> posts = new ArrayList<>();

                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    MusicPost musicPost = snap.getValue(MusicPost.class);
                    posts.add(musicPost);
                }

                callback.onComplete(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }


    interface IGetMusicPostCallback{
        void onComplete(MusicPost musicPost);
        void onCancel();
    }
    public void getPostByID (String postID, final IGetMusicPostCallback callback){
        postsReference.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MusicPost mv = dataSnapshot.getValue(MusicPost.class);
                callback.onComplete(mv);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }



}

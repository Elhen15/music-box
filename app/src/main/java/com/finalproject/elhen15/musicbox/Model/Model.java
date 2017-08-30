package com.finalproject.elhen15.musicbox.Model;

/**
 * Created by Elhen15 on 29/07/2017.
 */

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import java.util.ArrayList;
import java.util.Calendar;


public class Model {
    private ModelUserFirebase modelUserFirebase;
    private ModelPostFirebase modelPostFirebase;
    private ModelStorageFirebase modelStorageFirebase;

    public final static Model instance = new Model();
    public static User user = null;

    private Model(){
        modelUserFirebase = new ModelUserFirebase();
        modelPostFirebase = new ModelPostFirebase();
        modelStorageFirebase = new ModelStorageFirebase();
    }

    private ArrayList<MusicPost> data = new ArrayList<>();

    // Get all users

    public interface IGetAllUsersCallback{
        void onComplete(ArrayList<User> users);
    }

    public void getAllUsers(final IGetAllUsersCallback callback){
        modelUserFirebase.getAllUsers(new ModelUserFirebase.IGetAllUsersCallback() {
            @Override
            public void onComplete(ArrayList<User> users) {
                callback.onComplete(users);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    // Getting user - works with firebase

    public interface IGetCurrentUserCallback {
        void onComplete(User currentUser);
    }
    public void getCurrentUser(final IGetCurrentUserCallback callback) {
        modelUserFirebase.getCurrentUser(new ModelUserFirebase.IGetCurrentUserCallback() {
            @Override
            public void onComplete(User user) {
                callback.onComplete(user);
            }
        });
    }

    public interface IGetUserByIdCallback {
        void onComplete(User user);
        void onCancel();
    }
    public void getUserById(String id, final IGetUserByIdCallback callback) {
        modelUserFirebase.getUserById(id, new ModelUserFirebase.IGetUserByIdCallback() {
            @Override
            public void onComplete(User user) {
                callback.onComplete(user);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
    }


    public interface IGetUserLoginCallback {
        void onComplete(User user);
    }
    public void userLogin(String email, String password , final IGetUserLoginCallback callback) {
        modelUserFirebase.userLogin(email, password, new ModelUserFirebase.IGetUserLoginCallback() {
            @Override
            public void onComplete(User user) {
                Log.d("dev","onComplete Model userLogin");
                callback.onComplete(user);
            }
        });
    }


    public interface IRemoveUserCallback{
        void onComplete(boolean isSuccess);
    }

    public void removeUser(User user, final IRemoveUserCallback callback){
        modelUserFirebase.removeUser(user, new ModelUserFirebase.IRemoveUserCallback() {
            @Override
            public void onComplete(boolean isSuccess) {
                callback.onComplete(isSuccess);
            }
        });
    }

    public interface IUpdateUserCallback {
        void onComplete(boolean success);
    }
    public void updateUser(final User user, final IUpdateUserCallback callback) {
        modelUserFirebase.updateUser(user, new ModelUserFirebase.IUpdateUserCallback() {
            @Override
            public void onComplete(boolean isSuccess) {
                callback.onComplete(isSuccess);
            }

            @Override
            public void onCancel() {
            }
        });
    }




    // Adding user - works with firebase
    public interface IAddUser {
        void onComplete(User user);
        void onError(String reason);
    }
    public void addUser(User user, String password, final IAddUser callback ) {
        modelUserFirebase.addUser(user, password, new ModelUserFirebase.IAddUser() {
            @Override
            public void onComplete(User user) {
                Log.d("dev","Model - onComplete " +user.getEmail());
                callback.onComplete(user);
            }

            @Override
            public void onError(String reason) {
                Log.d("dev","Model - onError: "+ reason);
                callback.onError(reason);
            }
        });
    }


    // Adding music post, works with firebase

    public void addPost(MusicPost musicPost){
        modelPostFirebase.addPost(musicPost);
    }


    public interface IGetCommentsOfPostsCallback{
        void onComplete(ArrayList<Comment> comments);
        void onCancel();
    }

    public void getCommentsOfPosts(String PostId, final IGetCommentsOfPostsCallback callback)
    {
        modelPostFirebase.getCommentsOfPosts(PostId, new ModelPostFirebase.IGetCommentsOfPostCallback() {
            @Override
            public void onComplete(ArrayList<Comment> comments) {
                callback.onComplete(comments);
            }

            @Override
            public void onCancel() {

            }
        });
    }


    // works with firebase
    public interface IUpdatePostCallback {
        void onComplete(boolean success);
    }
    public void updatePost(final MusicPost musicPost, final IUpdatePostCallback callback){
        this.getPostByID(musicPost.id, new IGetPostCallback() {
            @Override
            public void onComplete(MusicPost otherMusicPost) {
                if (otherMusicPost == null) {
                    addPost(musicPost);
                    callback.onComplete(true);
                }
                else {
                    modelPostFirebase.editPost(musicPost, new ModelPostFirebase.IUpdatePostCallback() {
                        @Override
                        public void onComplete(boolean success) {
                            callback.onComplete(success);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                callback.onComplete(false);
            }
        });
    }

    // Getting list of all the posts
    public interface IGetAllPostsCallback {
        void onComplete(ArrayList<MusicPost> posts);
        void onCancel();
    }
    public void getAllPosts(final IGetAllPostsCallback callback){
        modelPostFirebase.getAllPosts(new ModelPostFirebase.IGetAlPostsCallback() {
            @Override
            public void onComplete(ArrayList<MusicPost> posts) {
                callback.onComplete(posts);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
    }


    public interface IGetPostCallback  {
        void onComplete(MusicPost musicPost);
        void onCancel();
    }

    // get post by id - firebase
    public void getPostByID (String postID, final IGetPostCallback callback){
        modelPostFirebase.getPostByID(postID, new ModelPostFirebase.IGetMusicPostCallback() {
            @Override
            public void onComplete(MusicPost musicPost) {
                callback.onComplete(musicPost);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
    }

    public Boolean removePost(MusicPost musicPost) {
        return data.remove(musicPost);
    }

    public Boolean editPost(MusicPost musicPost){
       // if (this.getPostByID(musicPost.getId()) == null) {
       //     this.addPost(musicPost);
       // }else {
            data.remove(musicPost);
            data.add(musicPost);
        //}

        return true;
    }

    public void userLogin(String email, String password) {
    }

    public void signOut(){
        modelUserFirebase.signOut();
    }


    // image functions
    public interface ISaveImageCallback {
        void onComplete(String imageUrl);
        void onCancel();
    }


    public void saveImage(final Bitmap imageBmp, final String name, final ISaveImageCallback callback) {
        modelStorageFirebase.saveImage(imageBmp, name, new ModelStorageFirebase.ISaveImageCallback() {
            @Override
            public void onComplete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                ModelFiles.saveImageToFile(imageBmp,fileName);
                callback.onComplete(url);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
    }

    public interface IGetImageCallback {
        void onComplete(Bitmap image);
        void onCancel();
    }

    public void getImage(final String url, final IGetImageCallback callback) {

        // If the local storage has the image
        final String fileName = URLUtil.guessFileName(url, null, null);

        ModelFiles.loadImageFromFileAsynch(fileName, new ModelFiles.LoadImageFromFileAsynch() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null){
                    Log.d("TAG","getImage from local success " + fileName);
                    callback.onComplete(bitmap);
                }else {
                    modelStorageFirebase.getImage(url, new ModelStorageFirebase.IGetImageCallback() {
                        @Override
                        public void onComplete(Bitmap image) {
                            String fileName = URLUtil.guessFileName(url, null, null);
                            Log.d("TAG","getImage from FB success " + fileName);
                            ModelFiles.saveImageToFile(image,fileName);
                            callback.onComplete(image);
                        }

                        @Override
                        public void onCancel() {
                            Log.d("TAG","getImage from FB fail ");
                            callback.onCancel();
                        }
                    });

                }
            }
        });

    }
}

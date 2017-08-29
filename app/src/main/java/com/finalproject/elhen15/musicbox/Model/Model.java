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

        //User user = new User("Elhen15@Gmail.com","222",true);

        /*for(int i = 0; i < 5; i++) {
            MusicPost musicPost= new MusicPost();
            musicPost.setTitle("Metallica " + i);
            musicPost.setDesc("bla bla bla vvivjfi i jifjif ifjijf ijfijfi \n fjijf ijfijf ijifj iji "+ i);
            musicPost.setUser(user);
            data.add(musicPost);
        }*/
    }

    private ArrayList<MusicPost> data = new ArrayList<>();

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
                    modelPostFirebase.editMovie(musicPost, new ModelPostFirebase.IUpdateMoveCallback() {
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
        void onComplete(ArrayList<MusicPost> movies);
        void onCancel();
    }
    public void getAllMovies(final IGetAllPostsCallback callback){
        modelPostFirebase.getAllMovies(new ModelPostFirebase.IGetAlPostsCallback() {
            @Override
            public void onComplete(ArrayList<MusicPost> movies) {
                callback.onComplete(movies);
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

    // get post bu id - firebase
    public void getPostByID (String movieID, final IGetPostCallback callback){
        modelPostFirebase.getPostByID(movieID, new ModelPostFirebase.IGetMusicPostCallback() {
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

    public void signUp(User user){
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

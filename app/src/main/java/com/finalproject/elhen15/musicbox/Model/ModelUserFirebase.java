package com.finalproject.elhen15.musicbox.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Elhen15 on 27/08/2017.
 */

public class ModelUserFirebase {

    private static final String USERS_KEY = "Users";
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private User currentUser;

    public ModelUserFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference(USERS_KEY);
    }

    // Section login user

    interface IGetUserByIdCallback {
        void onComplete(User user);
        void onCancel();
    }
    public void getUserById(String id, final IGetUserByIdCallback callback) {
        usersReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onComplete(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }



    interface IGetCurrentUserCallback {
        void onComplete(User user);
    }
    public void getCurrentUser(final IGetCurrentUserCallback callback) {
        // singleton
        if (currentUser != null) {
            callback.onComplete(currentUser);
        }
        else {
            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null) {
                callback.onComplete(null);
            }
            else {
                getUserById(firebaseUser.getUid(), new IGetUserByIdCallback() {
                    @Override
                    public void onComplete(User user) {
                        currentUser = new User(user);
                        currentUser.setId(firebaseUser.getUid());
                        callback.onComplete(currentUser);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        }
    }

    interface IGetUserLoginCallback {
        void onComplete(User user);
    }
    public void userLogin(String email, String password , final IGetUserLoginCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            callback.onComplete(null);
                        }
                        else {
                            getCurrentUser(new IGetCurrentUserCallback() {
                                @Override
                                public void onComplete(User user) {
                                    callback.onComplete(user);
                                }
                            });
                        }
                    }
                });
    }



    // Section adding new user
    interface IAddUser {
        void onComplete(User user);
        void onError(String reason);
    }
    public void addUser(final User user, String password, final IAddUser callback) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), "123456Ee!")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("dev","createUserWithEmail: onComplete()");
                        FirebaseUser firebaseUser = task.isSuccessful() ?
                                firebaseAuth.getCurrentUser() : null;

                        if (firebaseUser != null) {
                            User savedUser = new User(user);
                            savedUser.setId(firebaseUser.getUid());
                            usersReference.child(firebaseUser.getUid()).setValue(savedUser);
                            callback.onComplete(savedUser);
                        }
                        else {
                            Log.d("dev","error while insert user" + task.getException().getMessage());
                            callback.onError(task.getException().getMessage());
                        }
                    }
                });
    }

}

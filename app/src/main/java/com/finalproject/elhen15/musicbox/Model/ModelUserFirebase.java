package com.finalproject.elhen15.musicbox.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

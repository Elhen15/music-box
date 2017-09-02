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

import java.util.ArrayList;

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


    interface IRemoveUserCallback{
        void  onComplete(boolean isSuccess);
    }
    public void removeUser(User user, final  IRemoveUserCallback callback)
    {
        usersReference.child(user.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                callback.onComplete(databaseError == null);
            }
        });

    }

    interface IUpdateUserCallback{
        void onComplete(boolean isSuccess);
        void onCancel();
    }

    public void updateUser(User user,final IUpdateUserCallback callback){
        usersReference.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                callback.onComplete(databaseError == null);
            }
        });
    }

    // Get all users
    interface IGetAllUsersCallback{
        public void onComplete(ArrayList<User> users);
        public void onCancel();
    }

    public void getAllUsers(final IGetAllUsersCallback callback){
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users  = new ArrayList<>();

                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    users.add(user);
                }

                callback.onComplete(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
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
            Log.d("dev","getCurrentUser ModelUserFirebase ");

            if (firebaseUser == null) {
                callback.onComplete(null);
            }
            else {
                Log.d("dev","getCurrentUser ModelUserFirebase "+ firebaseUser.getUid());
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
                            Log.d("dev","onComplete userLogin ModelUserFirebase with null "+task.getException());
                            callback.onComplete(null);
                        }
                        else {
                            getCurrentUser(new IGetCurrentUserCallback() {
                                @Override
                                public void onComplete(User user) {
                                    Log.d("dev","onComplete userLogin ModelUserFirebase with user "+user.getEmail());
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
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
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

    public void signOut() {
        firebaseAuth.signOut();
        currentUser = null;
    }

}

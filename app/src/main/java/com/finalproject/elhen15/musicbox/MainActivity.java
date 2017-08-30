package com.finalproject.elhen15.musicbox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.Model.User;
import com.finalproject.elhen15.musicbox.fragments.AddOrEditFragment;
import com.finalproject.elhen15.musicbox.fragments.LoginFragment;
import com.finalproject.elhen15.musicbox.fragments.MusicPostDetails;
import com.finalproject.elhen15.musicbox.fragments.MusicPostListFragment;
import com.finalproject.elhen15.musicbox.fragments.SignUpFragment;
import com.finalproject.elhen15.musicbox.fragments.UserAdminFragment;

public class MainActivity extends ActionBarActivity implements
        LoginFragment.OnFragmentInteractionListener,
        MusicPostListFragment.OnListFragmentInteractionListener,
        AddOrEditFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener,
        MusicPostDetails.OnFragmentInteractionListener,
        UserAdminFragment.OnListFragmentInteractionListener{

    LoginFragment loginFragmentInstance;

    public static Fragment musicPostListFragmentInstance;
    public static Fragment musicPostDetailsFragmentInstance;

    private static Context context;

    public static FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LoginFragment loginFragment = LoginFragment.newInstance();
        transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_container, loginFragment);
        transaction.commit();
    }

    public static Context getContext(){
        return context;
    }

    // creates the list fragment
    @Override
    public void onFragmentInteractionChangeFrag(Fragment frag) {
        if (frag instanceof MusicPostListFragment)
            this.musicPostListFragmentInstance =  frag;
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, frag);
        transaction.addToBackStack("backLogin");
        transaction.commit();
    }


    // creates the details fragment
    @Override
    public void onListFragmentInteraction(MusicPost item) {
        this.musicPostDetailsFragmentInstance = MusicPostDetails.newInstance(item.getId());

        transaction = getFragmentManager().beginTransaction();
        transaction.hide(this.musicPostListFragmentInstance);
        transaction.replace(R.id.main_container, this.musicPostDetailsFragmentInstance);
        transaction.addToBackStack("backMusicPostList");
        transaction.commit();
    }

    // create add or edit fragment
    @Override
    public void onFragmentInteraction(String musicPostID) {
        AddOrEditFragment details = AddOrEditFragment.newInstance(musicPostID, "Edit");

        transaction = getFragmentManager().beginTransaction();
        transaction.hide(this.musicPostDetailsFragmentInstance);
        transaction.addToBackStack("backPostDetails");
        transaction.replace(R.id.main_container, details).commit();
    }


    // create the list fragment
    @Override
    public void onFragmentInteractionAddOrEdit() {
        MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1, Model.user.getIsAdmin());
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, listFragment);
        transaction.addToBackStack("backMusicPostList");
        transaction.commit();
    }

    // Empty function - must implement
    @Override
    public void onListFragmentInteraction(User user){}

  /*  @Override
    public void OnFragmentAdminChangeFrag(Fragment fragment)
    {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("backList");
        transaction.commit();
    }
*/
    // create sign up fragment
   /* @Override
    public void onFragmentSignUpInteraction() {
        SignUpFragment signUpFragment = SignUpFragment.newInstance();
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container,signUpFragment);
        transaction.addToBackStack("backLogin");
        transaction.commit();
    }*/

}

package com.finalproject.elhen15.musicbox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.fragments.AddOrEditFragment;
import com.finalproject.elhen15.musicbox.fragments.LoginFragment;
import com.finalproject.elhen15.musicbox.fragments.MusicPostDetails;
import com.finalproject.elhen15.musicbox.fragments.MusicPostListFragment;
import com.finalproject.elhen15.musicbox.fragments.SignUpFragment;

public class MainActivity extends ActionBarActivity implements LoginFragment.OnFragmentInteractionListener,
        MusicPostListFragment.OnListFragmentInteractionListener, AddOrEditFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener,
        MusicPostDetails.OnFragmentInteractionListener{

    Fragment musicPostListFragmentInstance;
    Fragment musicPostDetailsFragmentInstance;

    public static FragmentTransaction tran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LoginFragment listFragment = LoginFragment.newInstance();
        tran = getFragmentManager().beginTransaction();
        tran.add(R.id.main_container, listFragment);
        tran.commit();
    }

    @Override
    public void onFragmentInteractionChangeFrag(Fragment frag) {
        if (frag instanceof MusicPostListFragment)
            this.musicPostListFragmentInstance =  frag;
        tran = getFragmentManager().beginTransaction();
        tran.addToBackStack("backLogin");
        tran.replace(R.id.main_container, frag);
        tran.commit();
    }

    @Override
    public void onListFragmentInteraction(MusicPost item) {
        this.musicPostDetailsFragmentInstance = MusicPostDetails.newInstance(item.getId());

        tran = getFragmentManager().beginTransaction();
        tran.hide(this.musicPostListFragmentInstance);

        tran.addToBackStack("backList");
        tran.add(R.id.main_container, this.musicPostDetailsFragmentInstance);
        tran.commit();
    }

    @Override
    public void onFragmentInteraction(int musicPostID) {
        AddOrEditFragment details = AddOrEditFragment.newInstance(musicPostID, "Edit");

        tran.hide(this.musicPostDetailsFragmentInstance);
        tran.addToBackStack("backToDetail");
        tran = getFragmentManager().beginTransaction();
        tran.add(R.id.main_container, details).commit();
    }


    @Override
    public void onFragmentInteractionAddOrEdit() {
        MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1);
        tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, listFragment);
        tran.commit();
    }

    @Override
    public void onFragmentSignUpInteraction() {
        SignUpFragment signUpFragment = SignUpFragment.newInstance();
        tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_container,signUpFragment);
        tran.commit();
    }
}

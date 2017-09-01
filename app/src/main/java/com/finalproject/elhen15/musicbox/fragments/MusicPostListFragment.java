package com.finalproject.elhen15.musicbox.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.finalproject.elhen15.musicbox.MainActivity.transaction;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MusicPostListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private LoginFragment.OnFragmentInteractionListener mLoginListener;
    public static FragmentTransaction tran;
    public RecyclerView recyclerView;
    public static List<MusicPost> postList;
    public MusicPostListFragment musicPostListFragment;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MusicPostListFragment() {
        musicPostListFragment = this;
    }

    public static MusicPostListFragment newInstance(int columnCount,boolean isAdmin) {
        MusicPostListFragment fragment = new MusicPostListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_post_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (MusicPostListFragment.postList!= null) {
                recyclerView.setAdapter(new MyMusicPostListRecyclerViewAdapter(MusicPostListFragment.postList, mListener));
            } else
                Model.instance.getAllPosts(new Model.IGetAllPostsCallback() {
                    @Override
                    public void onComplete(ArrayList<MusicPost> posts) {
                        recyclerView.setAdapter(new MyMusicPostListRecyclerViewAdapter(posts, mListener));
                        MusicPostListFragment.postList = posts;
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        if (context instanceof LoginFragment.OnFragmentInteractionListener ){
            mLoginListener =(LoginFragment.OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() +" must implement onListToLoginInteractionListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MusicPost item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        menu.clear();
        // If the user choose to see only his posts
        if (MusicPostListFragment.postList != null)
            inflater.inflate(R.menu.menu_main_all_posts, menu);
        // If the user is admin
        else if (Model.user.getIsAdmin())
            inflater.inflate(R.menu.menu_main_admin,menu);

        // Simple user - first login
        else
            inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new:
                Log.d("dev", "will create new ");
                AddOrEditFragment details = AddOrEditFragment.newInstance(0+"", "Add");
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, details).commit();
                break;
            case android.R.id.home:
                MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1,Model.user.getIsAdmin());
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, listFragment);
                tran.commit();
                break;
            case R.id.signout:
                Log.d("dev","sign out the user");
                Model.instance.signOut();
                LoginFragment loginFragment = LoginFragment.newInstance();
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, loginFragment);
                tran.commit();

                break;
            case R.id.my_posts:
                showOnlyUserPosts();
                break;
            case R.id.my_likes:
                showOnlyUserLikes();
                break;
            case R.id.all_posts:
                MusicPostListFragment.postList = null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(musicPostListFragment).attach(musicPostListFragment).commit();
                break;
            case R.id.usersAdmin:
                UserAdminFragment userAdminFragment = UserAdminFragment.newInstance(1);
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container,userAdminFragment).commit();
                break;
            case R.id.sortPosts:
                Collections.sort(MusicPostListFragment.postList, new Comparator<MusicPost>() {
                    @Override
                    public int compare(MusicPost musicPost, MusicPost t1) {
                        return musicPost.compareTo(t1);
                    }
                });
                break;
            default:
                break;
        }

        return true;
    }

    public void showOnlyUserPosts() {
        Model.instance.getAllPosts(new Model.IGetAllPostsCallback() {
            @Override
            public void onComplete(ArrayList<MusicPost> posts) {
                ArrayList<MusicPost> postToRemove = new ArrayList<MusicPost>();

                for (MusicPost musicPost: posts) {
                    if (!musicPost.getUser().getId().equals(Model.user.getId())) {
                        Log.d("dev", "only user posts removing post " + musicPost.getId());
                        postToRemove.add(musicPost);
                    }
                }

                for (MusicPost musicPost: postToRemove
                     ) {
                    posts.remove(musicPost);
                }
                MusicPostListFragment.postList = posts;

                Log.d("dev","Refresh fragment");
                // Refresh the fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(musicPostListFragment).attach(musicPostListFragment).commit();
            }

            @Override
            public void onCancel() {
            }
        });
    }


    public void showOnlyUserLikes() {
        Model.instance.getAllPosts(new Model.IGetAllPostsCallback() {
            @Override
            public void onComplete(ArrayList<MusicPost> posts) {
                ArrayList<MusicPost> postToLike = new ArrayList<MusicPost>();

             /*   for (MusicPost musicPost: posts) {
                    for (MusicPost postLike: Model.user.getPostLikes()) {
                        if(musicPost.getId().equals(postLike.getId())){
                            postToLike.add(musicPost);
                        }
                    }
                }*/

                /*for (MusicPost musicPost: postToLike) {
                    posts.clear();
                    posts.add(musicPost);
                }*/
                MusicPostListFragment.postList = Model.user.getPostLikes();

                // Refresh the fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(musicPostListFragment).attach(musicPostListFragment).commit();
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
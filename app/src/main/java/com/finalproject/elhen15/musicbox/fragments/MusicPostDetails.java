package com.finalproject.elhen15.musicbox.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.finalproject.elhen15.musicbox.R;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.Model.Model;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicPostDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicPostDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPostDetails extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POST_ID= "POST_ID";

    // TODO: Rename and change types of parameters
    private String MusicPostID;

    private OnFragmentInteractionListener mListener;
    public static FragmentTransaction tran;

    public TextView postTitle;
    public TextView postDescription;
    public TextView dateCreated;
    public TextView likesCount;

    public MusicPostDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postID Parameter 1.
     * @return A new instance of fragment MusicPostDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicPostDetails newInstance(String postID) {
        MusicPostDetails fragment = new MusicPostDetails();
        Bundle args = new Bundle();
        args.putString(POST_ID, postID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MusicPostID = getArguments().getString(POST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("dev","onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_post_details, container, false);

        final LinearLayout l = (LinearLayout) view.findViewById(R.id.detail_frag);

        MusicPost currentPost = Model.instance.getPostByID(MusicPostID);

        postTitle = (TextView) view.findViewById(R.id.details_music_name);
        postTitle.setText(currentPost.getTitle());

        postDescription = (TextView) view.findViewById(R.id.details_music_description);
        postDescription.setText(getString(R.string.description)+": "+currentPost.getDesc());

        dateCreated = (TextView) view.findViewById(R.id.details_music_create);
        dateCreated.setText(getString(R.string.date_created)+": "+currentPost.getDate().toString());

        likesCount = (TextView) view.findViewById(R.id.details_music_likes);
        likesCount.setText(getString(R.string.likes_counter)+": "+currentPost.getLikesCount()+"");

        WebView myWebView = (WebView) view.findViewById( R.id.youtube_webview);
        myWebView.setWebChromeClient(new WebChromeClient());
        WebSettings ws = myWebView.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        //myWebView.loadUrl("http://www.youtube.com/embed/bIPcobKMB94");

        //String playVideo= "<html><body>Youtube video .. <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/bIPcobKMB94\" frameborder=\"0\"></body></html>";

        //myWebView.loadData(playVideo, "text/html", "utf-8");


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        mListener.onFragmentInteraction(MusicPostID);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String postId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                mListener.onFragmentInteraction(MusicPostID);

                break;
            case android.R.id.home:
                MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1);
                tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, listFragment);
                tran.commit();
                break;
            default:
                break;
        }

        return true;
    }

}
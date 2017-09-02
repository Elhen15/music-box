package com.finalproject.elhen15.musicbox.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.finalproject.elhen15.musicbox.Model.User;
import com.finalproject.elhen15.musicbox.Utiles.Functions;
import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.R;

import static android.app.Activity.RESULT_OK;
import static com.finalproject.elhen15.musicbox.fragments.MusicPostDetails.tran;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddOrEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddOrEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class AddOrEditFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSTID = "POSTID";
    private static final String ARG_ACTION = "ACTION";
    private static final String DEFAULT_IMAGE = "https://firebasestorage.googleapis.com/v0/b/musicbox-80a7d.appspot.com/o/blank.png?alt=media&token=2d0d8ae4-31ee-44e1-bccb-69b2c547d788";

    private String POSTID;
    private String ACTION;
    private static Button btnAddEdit = null;
    private static MusicPost musicPost;
    private static EditText edtTitle = null;
    private static EditText edtDesc = null;
    private static ProgressBar progressBar;
    private static ImageView edtImage = null;
    private Bitmap musicImageBitmap ;
    boolean isSuccesUpload = false;


    private OnFragmentInteractionListener mListener;

    public AddOrEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param POSTID Parameter 1.
     * @param ACTION Parameter 2.
     * @return A new instance of fragment AddOrEditFragment.
     */
    public static AddOrEditFragment newInstance(String POSTID, String ACTION) {
        AddOrEditFragment fragment = new AddOrEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POSTID, POSTID);
        args.putString(ARG_ACTION, ACTION);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            POSTID = getArguments().getString(ARG_POSTID);
            ACTION = getArguments().getString(ARG_ACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG","onCreateView - AddOrEditFragment");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_or_edit, container, false);
        btnAddEdit = (Button) v.findViewById(R.id.AddEditButton);
        Button btnAddEditCancel = (Button) v.findViewById(R.id.AddEditButtonCancel);
        Button btnAddEditDel = (Button) v.findViewById(R.id.AddEditButtonDel);
        edtTitle = (EditText) v.findViewById(R.id.AddEditTitle);
        edtDesc = (EditText) v.findViewById(R.id.AddEditDescription);
        edtImage = (ImageView) v.findViewById(R.id.AddEditImage);
        progressBar =(ProgressBar) v.findViewById(R.id.progressBar2);


        if (ACTION.equals("Add")) {
            btnAddEdit.setText("Add");
            musicPost = new MusicPost();
        }else {
            btnAddEdit.setText("Edit");
            btnAddEditDel.setVisibility(View.VISIBLE);
            Model.instance.getPostByID(POSTID, new Model.IGetPostCallback() {
                @Override
                public void onComplete(MusicPost musicPostCopy) {
                    musicPost = musicPostCopy;
                    edtTitle.setText(musicPost.getTitle());
                    edtDesc.setText(musicPost.getDesc());
                }

                @Override
                public void onCancel() {

                }
            });

        }

        btnAddEditDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progressBar.setVisibility(View.VISIBLE);
                musicPost.setIsDeleted(true);
                Model.instance.editPost(musicPost, new Model.IEditPostCallback() {
                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        Functions.alertMessage(v,"Message","Music post has been deleted :/");
                        mListener.onFragmentInteractionAddOrEdit();
                    }
                    public void onCancel(){}
                });
                }
            });

                btnAddEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteractionAddOrEdit();
            }
        });

        btnAddEdit.setOnClickListener(this);

        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        return v;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            musicImageBitmap = (Bitmap) extras.get("data");
            edtImage.setImageBitmap(musicImageBitmap);
        }
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
    public void onClick(final View v) {

        musicPost = new MusicPost();
        musicPost.setUser(Model.user);
        musicPost.setTitle(edtTitle.getText().toString());
        musicPost.setDesc(edtDesc.getText().toString());

        // if the default image is
        if (musicImageBitmap == null) {
            musicPost.setImageUrl(DEFAULT_IMAGE);
        }
        else {
            long timeStamp = System.currentTimeMillis();
            String imageName = musicPost.getTitle() + "-" + timeStamp + ".jpeg";
            progressBar.setVisibility(View.VISIBLE);
            Model.instance.saveImage(musicImageBitmap, imageName, new Model.ISaveImageCallback() {
                @Override
                public void onComplete(String imageUrl) {
                    Log.d("dev", "onComplete addoreditfragment saveImage " + imageUrl + " " + isSuccesUpload);
                    musicPost.setImageUrl(imageUrl);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancel() {

                }
            });
        }

        if (ACTION.equals("Add")) {
            Model.instance.addPost(musicPost);
            // for the next time
            Functions.alertMessage(v, "Message", "Music post has been added! : )");
            mListener.onFragmentInteractionAddOrEdit();
            }
        else {
            musicPost.setId(POSTID);
            progressBar.setVisibility(View.VISIBLE);
            Model.instance.getPostByID(POSTID, new Model.IGetPostCallback() {
                @Override
                public void onComplete(MusicPost post) {
                    musicPost.setLikesCount(post.getLikesCount());
                    musicPost.setImageUrl(post.getImageUrl());
                    Model.instance.editPost(musicPost, new Model.IEditPostCallback() {
                        @Override
                        public void onComplete() {
                            progressBar.setVisibility(View.GONE);
                            Functions.alertMessage(v, "Message", "Music post has been edited! : )");
                            mListener.onFragmentInteractionAddOrEdit();
                        }
                        @Override
                        public void onCancel() {
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
        }
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
        void onFragmentInteractionAddOrEdit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (ACTION) {
                    // return to list mode
                    case "Add":
                        MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1,Model.user.getIsAdmin());
                        tran = getFragmentManager().beginTransaction();
                        tran.replace(R.id.main_container, listFragment);
                        tran.commit();
                        break;

                    // return to view mode
                    case "Edit":
                        MusicPostDetails details = MusicPostDetails.newInstance(POSTID);
                        tran = getFragmentManager().beginTransaction();
                        tran.replace(R.id.main_container, details).commit();
                        break;
                }
                break;
            default:
                break;
        }

        return true;
    }
}
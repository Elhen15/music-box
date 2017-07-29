package com.finalproject.elhen15.musicbox.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.R;

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

    // TODO: Rename and change types of parameters
    private String POSTID;
    private String ACTION;
    private static Button btnAddEdit = null;
    private static MusicPost musicPost;
    private static EditText edtName = null;
    private static EditText edtId = null;
    private static EditText edtRate = null;
    private static ImageView edtImage = null;
    private static CheckBox edtCb = null;

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
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_or_edit, container, false);
        btnAddEdit = (Button) v.findViewById(R.id.AddEditButton);
        Button btnAddEditCancel = (Button) v.findViewById(R.id.AddEditButtonCancel);
        Button btnAddEditDel = (Button) v.findViewById(R.id.AddEditButtonDel);
        edtName = (EditText) v.findViewById(R.id.AddEditName);
        edtId = (EditText) v.findViewById(R.id.AddEditId);
        edtRate = (EditText) v.findViewById(R.id.AddEditRate);
        edtImage = (ImageView) v.findViewById(R.id.AddEditImage);
        edtCb = (CheckBox) v.findViewById(R.id.AddEditCB);

        if (ACTION.equals("Add")) {
            btnAddEdit.setText("Add");
            musicPost = new MusicPost();
        }else {
            btnAddEdit.setText("Edit");
            btnAddEditDel.setVisibility(View.VISIBLE);
            musicPost = Model.instance.getPostByID(POSTID);
            edtId.setText(musicPost.getId());
            edtName.setText(musicPost.getTitle());
        }

        btnAddEditDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Model.instance.removePost(musicPost)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("POST DELETED");
                    alertDialog.setMessage("SUCCESS");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                mListener.onFragmentInteractionAddOrEdit();
            }
        });

        btnAddEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteractionAddOrEdit();
            }
        });

        btnAddEdit.setOnClickListener(this);

        return v;
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
        musicPost.setTitle(edtName.getText().toString());
        String idToCheck = edtId.getText().toString();
        musicPost.setImageUrl("../res/drawable/grid.png");

        if (((Model.instance.getPostByID(idToCheck) != null) && (btnAddEdit.getText().equals("Add"))) ||
                ((!idToCheck.equals(musicPost.getId())) && Model.instance.getPostByID(idToCheck) != null) &&
                        (btnAddEdit.getText().equals("Edit")))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
            alertDialog.setTitle("ID IN USE");
            alertDialog.setMessage("Choose another id");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            musicPost.setId(idToCheck);

            if (Model.instance.editPost(musicPost)) {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                alertDialog.setTitle("POST SAVED");
                alertDialog.setMessage("SUCCESS");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            mListener.onFragmentInteractionAddOrEdit();
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
                        MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1);
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
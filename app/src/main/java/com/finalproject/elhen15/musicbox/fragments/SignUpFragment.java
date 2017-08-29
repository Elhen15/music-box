package com.finalproject.elhen15.musicbox.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.Model.User;
import com.finalproject.elhen15.musicbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private static View v;
    private static EditText txtEmail;
    private static EditText txtPassword;
    private static EditText txtConfirmPassword;
    private static CheckBox cbAgree;
    private static Button btnSignUp;


    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        txtEmail = (EditText) v.findViewById(R.id.userEmailId);
        txtPassword = (EditText) v.findViewById(R.id.password);
        txtConfirmPassword = (EditText) v.findViewById(R.id.confirmPassword);
        cbAgree = (CheckBox) v.findViewById(R.id.terms_conditions);
        btnSignUp = (Button) v.findViewById(R.id.signUpBtn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog DialogToShow = validate();

                if (DialogToShow != null)
                    DialogToShow.show();
                else
                    SignUp();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Fragment frag) {
        if (mListener != null) {
            mListener.onFragmentInteractionChangeFrag(frag);
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
        void onFragmentInteractionChangeFrag(Fragment frag);
    }

    public AlertDialog validate(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get all edittext texts
        String getEmailId = txtEmail.getText().toString();
        String getPassword = txtPassword.getText().toString();
        String getConfirmPassword = txtConfirmPassword.getText().toString();

        Pattern p = Patterns.EMAIL_ADDRESS;
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            builder.setMessage("All fields are required.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Check if email id valid or not
        else if (!m.find())
            builder.setMessage("Your Email Id is Invalid.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            txtEmail.requestFocus();
                        }
                    });

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            builder.setMessage("Both password doesn't match.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            txtPassword.requestFocus();
                        }
                    });

            // Make sure user should check Terms and Conditions checkbox
        else if (!cbAgree.isChecked())
            builder.setMessage("Please select Terms and Conditions.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });
        else
            return null;


        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void SignUp(){
        final User newUser = new User(txtEmail.getText().toString(),
                txtPassword.getText().toString(),
                false);

        String password = txtPassword.getText().toString();

        Model.instance.addUser(newUser, password, new Model.IAddUser() {
            @Override
            public void onComplete(User user) {
                MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1,false);
                onButtonPressed(listFragment);
            }

            @Override
            public void onError(String reason) {
                Toast.makeText(v.getContext(), "Unable to create user, Please try again", Toast.LENGTH_LONG);
            }
        });
    }
}

package com.finalproject.elhen15.musicbox.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ProgressBar;

import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.Model.User;
import com.finalproject.elhen15.musicbox.R;
import com.finalproject.elhen15.musicbox.Utiles.Functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends android.app.Fragment {
    private OnFragmentInteractionListener mListener;

    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        Model.instance.getCurrentUser(new Model.IGetCurrentUserCallback(){
            @Override
            public void onComplete(User currUser){
                if(currUser==null){
                    actuallyCreateTheView(view);
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    Model.user = currUser;
                    MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1,Model.user.getIsAdmin());
                    onButtonPressed(listFragment);
                }
            }
        });

        /*
        Button btnSignIn = (Button) view.findViewById(R.id.loginbtn_signin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPostListFragment listFragment =  MusicPostListFragment.newInstance(1);
                onButtonPressed(listFragment);
            }
        });*/

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Fragment fragment) {
        if (mListener != null) {
            mListener.onFragmentInteractionChangeFrag(fragment);
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
        void onFragmentInteractionChangeFrag(Fragment fragment);
    }


    private AlertDialog checkFieldValidation(String email, String password) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Pattern p = Patterns.EMAIL_ADDRESS;
        Matcher m = p.matcher(email);

        if (email.equals("") || email.length() == 0
                || password.equals("") || password.length() == 0)

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
                            emailEditText.requestFocus();
                        }
                    });
        else {
            return null;
        }

        return builder.create();
    }

    private void actuallyCreateTheView(View view) {
        emailEditText = (EditText) view.findViewById(R.id.editText_username);
        passwordEditText = (EditText) view.findViewById(R.id.editText_pw);

        Button btnSignIn = (Button) view.findViewById(R.id.loginbtn_signin);
        Button btnSignUp = (Button) view.findViewById(R.id.loginbtn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = SignUpFragment.newInstance();
                onButtonPressed(signUpFragment);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("dev","onClick listener LoginFragment");
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                AlertDialog alertDialog = checkFieldValidation(email, password);

                if (alertDialog != null) {
                    alertDialog.show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    Model.instance.userLogin(email, password, new Model.IGetUserLoginCallback() {
                        @Override
                        public void onComplete(User user) {
                            progressBar.setVisibility(View.GONE);

                            Log.d("dev","onComplete - UserLogin LoginFragment: "+ user.getEmail());
                            if (user != null) {
                                Model.user = user;
                                MusicPostListFragment listFragment = MusicPostListFragment.newInstance(1,user.getIsAdmin());
                                onButtonPressed(listFragment);
                            } else {
                                Functions.alertMessage(v, "Authentication failed", "Please try again");
                            }
                        }
                    });
                }
            }
        });
    }
}

package com.finalproject.elhen15.musicbox.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.Model.User;
import com.finalproject.elhen15.musicbox.R;
import com.finalproject.elhen15.musicbox.fragments.UserAdminFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<User> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyUserRecyclerViewAdapter(ArrayList<User> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_useradmin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User user =  mValues.get(position);

        holder.mItem = user;
        holder.mUserName.setText(holder.mUserName.getText()+user.getEmail().substring(0,user.getEmail().indexOf("@")));
        holder.mPassword.setText(holder.mPassword.getText()+user.getPassword());
        holder.mItem = mValues.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final Button mBtnAdmin;
        public final Button mBtnDelete;
        public final TextView mPassword;
        public final TextView mUserName;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPassword = (TextView) view.findViewById(R.id.user_password);
            mUserName = (TextView) view.findViewById(R.id.user_name);
            mBtnAdmin = (Button) view.findViewById(R.id.btnadmin);
            mBtnDelete = (Button) view.findViewById(R.id.btndeleteuser);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUserName.getText() + "'";
        }
    }
}

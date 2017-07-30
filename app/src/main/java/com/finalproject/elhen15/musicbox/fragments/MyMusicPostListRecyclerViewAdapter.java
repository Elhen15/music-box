package com.finalproject.elhen15.musicbox.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finalproject.elhen15.musicbox.R;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.fragments.MusicPostListFragment.OnListFragmentInteractionListener;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MusicPost} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMusicPostListRecyclerViewAdapter extends RecyclerView.Adapter<MyMusicPostListRecyclerViewAdapter.ViewHolder> {

    private final List<MusicPost> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMusicPostListRecyclerViewAdapter(List<MusicPost> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_musicpostlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MusicPost wantedPost =  mValues.get(position);

        holder.mItem = wantedPost;
        holder.mIdView.setText(wantedPost.getId());
        holder.mContentView.setText(wantedPost.getTitle());
        holder.mUserName.setText(wantedPost.getUser().getEmail());

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
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mUserName;
        public MusicPost mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.strow_id);
            mContentView = (TextView) view.findViewById(R.id.strow_name);
            mUserName = (TextView) view.findViewById(R.id.author_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
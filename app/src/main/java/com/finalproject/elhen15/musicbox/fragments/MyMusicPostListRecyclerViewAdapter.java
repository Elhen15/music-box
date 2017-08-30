package com.finalproject.elhen15.musicbox.fragments;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finalproject.elhen15.musicbox.Model.Model;
import com.finalproject.elhen15.musicbox.R;
import com.finalproject.elhen15.musicbox.Model.MusicPost;
import com.finalproject.elhen15.musicbox.Utiles.Functions;
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
        final MusicPost wantedPost =  mValues.get(position);

        holder.mItem = wantedPost;
        holder.mIdView.setText(wantedPost.getId()+"");
        holder.mContentView.setText(wantedPost.getTitle());
        holder.mUserName.setText(wantedPost.getUser().getEmail());
        holder.mLikeButton.setOnClickListener(new View.OnClickListener(){

            // OnClick function for the like button
            public void onClick(final View v){
                if (null != mListener){
                    if (isAlreadyLikeIt(wantedPost))
                    {
                        Functions.alertMessage(v, "Sorry", "You already like it..");
                    }
                    else {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        Model.user.addPostLike(wantedPost);
                        Model.instance.updateUser(Model.user, new Model.IUpdateUserCallback() {
                            @Override
                            public void onComplete(boolean success) {
                                holder.mItem.setLikesCount(holder.mItem.getLikesCount() + 1);
                                Model.instance.updatePost(wantedPost, new Model.IUpdatePostCallback() {
                                    @Override
                                    public void onComplete(boolean success) {
                                        holder.progressBar.setVisibility(View.GONE);
                                        Functions.alertMessage(v, "You like it!", ":)");

                                    }
                                });
                            }
                        });
                    }
                }
            }
        } );

        holder.mImageView.setTag(holder.mItem.getImageUrl());

        if (holder.mItem.getImageUrl() != null && !holder.mItem.getImageUrl().isEmpty() &&
                !holder.mItem.getImageUrl().equals("")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Model.instance.getImage(wantedPost.getImageUrl(), new Model.IGetImageCallback() {
                @Override
                public void onComplete(Bitmap image) {
                    String tagUrl = holder.mImageView.getTag().toString();
                    if (tagUrl.equals(holder.mItem.getImageUrl())) {
                        holder.mImageView.setImageBitmap(image);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancel() {
                }
            });
        }

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

    public boolean isAlreadyLikeIt(MusicPost wantedPost)
    {
        for (MusicPost musicPost: Model.user.getPostLikes())
        {
            if (musicPost.getId().equals(wantedPost.getId()))
            {
                return true;
            }
        }
        return false;
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
        public final ImageButton mLikeButton;
        public final ImageView mImageView;
        public MusicPost mItem;
        public final ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            mImageView =(ImageView)view.findViewById(R.id.strow_image);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.strow_id);
            mContentView = (TextView) view.findViewById(R.id.strow_name);
            mUserName = (TextView) view.findViewById(R.id.author_name);
            mLikeButton = (ImageButton) view.findViewById(R.id.imageButton);
            progressBar = (ProgressBar)view.findViewById(R.id.post_progressBar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
package com.example.popularmoviesstage1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.models.Review;

import java.util.List;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {

    private static final String TAG = "ReviewAdapter";
    private Context mContext;
    private List<Review> mReviewData;

    public ReviewRecyclerViewAdapter(Context mContext, List<Review> mReviewData) {
        this.mContext = mContext;
        this.mReviewData = mReviewData;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.review_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, final int position) {

        Review currentReview = mReviewData.get(position);

        holder.mUser.setText(currentReview.getAuthor());

        holder.mReview.setText(currentReview.getContent());

    }

    @Override
    public int getItemCount() {
        return (mReviewData.size() > 5) ? 5 : mReviewData.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mUser;
        public TextView mReview;
        public RelativeLayout parentLayout;

        ReviewViewHolder(View itemView) {
            super(itemView);

            mUser = itemView.findViewById(R.id.user_textView);
            mReview = itemView.findViewById(R.id.review_textView);
            parentLayout = itemView.findViewById(R.id.review_parent_layout);
        }
    }
}

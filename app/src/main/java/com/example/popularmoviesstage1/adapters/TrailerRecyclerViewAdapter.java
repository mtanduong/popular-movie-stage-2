package com.example.popularmoviesstage1.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.models.Video;

import java.util.List;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.TrailerViewHolder>{

    private static final String TAG = "TrailerAdapter";
    private Context mContext;
    private List<Video> mTrailerData;


    public TrailerRecyclerViewAdapter(Context mContext, List<Video> mTrailerData) {
        this.mContext = mContext;
        this.mTrailerData = mTrailerData;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, final int position) {

        final Video currentVideo = mTrailerData.get(position);

        holder.thumbTxt.setText(currentVideo.getmName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                watchYoutubeVideo(mContext, currentVideo.getmKey());

                String itemClicked = mTrailerData.get(position).getmName();
                Toast.makeText(mContext, "You clicked: " + itemClicked, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTrailerData.size() > 10) ? 10 : mTrailerData.size();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbImg;
        public TextView thumbTxt;
        public RelativeLayout parentLayout;

        TrailerViewHolder(View itemView) {
            super(itemView);

            thumbImg = itemView.findViewById(R.id.trailer_imageView);
            thumbTxt = itemView.findViewById(R.id.trailer_textView);
            parentLayout = itemView.findViewById(R.id.trailer_parent_layout);
        }
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));

        Log.d(TAG, "watchYoutubeVideo: vnd.youtube:" + id);
        Log.d(TAG, "watchYoutubeVideo: http://www.youtube.com/watch?v=" + id);

        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}

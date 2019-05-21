package com.example.popularmoviesstage1.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.activities.DetailActivity;
import com.example.popularmoviesstage1.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private static final String TAG = "MovieRecyclerViewAdapter";
    private Context mContext;
    private List<Movie> mMovieData = new ArrayList<>();
    public static final int ADD_FAVORITE_REQUEST = 1;

    public MovieRecyclerViewAdapter(Context mContext, List<Movie> mMovieData) {

        this.mContext = mContext;
        this.mMovieData = mMovieData;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.grid_item, parent, false);

        return new MovieViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + mMovieData.get(position).getThumbnailImgUrl())
                .resize(650, 1000)
                .centerInside()
                .into(holder.thumbImg);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);

                intent.putExtra("Movie Detail", mMovieData.get(position));

                String itemClicked = mMovieData.get(position).getTitle();
                Toast.makeText(mContext, "You clicked: " + itemClicked, Toast.LENGTH_SHORT).show();

                ((Activity) mContext).startActivityForResult(intent, ADD_FAVORITE_REQUEST);
                //mContext.startActivity(intent);

                }
        });

    }

    @Override
    public int getItemCount() {
        //return (mMovieData == NULL) ? 0 : mMovieData.size();
        return mMovieData.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbImg;
        RelativeLayout parentLayout;

        MovieViewHolder(View itemView) {
            super(itemView);

            thumbImg = itemView.findViewById(R.id.posterImg);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }

    public void setMovies(List<Movie> movies) {

        this.mMovieData =  movies;
        notifyDataSetChanged();
    }

}
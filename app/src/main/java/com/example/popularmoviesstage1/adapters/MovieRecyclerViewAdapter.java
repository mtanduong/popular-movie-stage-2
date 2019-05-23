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
import java.util.Collection;
import java.util.List;

import static java.sql.Types.NULL;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private static final String TAG = "MovieAdapter";
    private Context mContext;
    private List<Movie> mMovieData;
    private List<Movie> mFavoriteData;
    private boolean favorite;
    public static final int ADD_FAVORITE_REQUEST = 1;

    public MovieRecyclerViewAdapter(Context mContext, List<Movie> mMovieData, List<Movie> mFavoriteData) {

        this.mContext = mContext;
        this.mMovieData = mMovieData;
        this.mFavoriteData = mFavoriteData;
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
                Log.d(TAG, "MovieAdapter/onClick: Saving movie object into Intent");
                if (containsMovie(mFavoriteData, mMovieData.get(position).getId())) {
                    Log.d(TAG, "MovieAdapter/onClick/favoriteCheck: " + mMovieData.get(position).getTitle() + " is a favorite. Set fav flag to true");
                    favorite = true;
                } else {
                    Log.d(TAG, "MovieAdapter/onClick/favoriteCheck: " + mMovieData.get(position).getTitle() + " is not a favorite. Set fav flag to false");
                    favorite = false;
                }

                intent.putExtra("Favorite", favorite);
                Log.d(TAG, "MovieAdapter/onClick: Saving favorite flag into Intent. Flag set as " + favorite);

                String itemClicked = mMovieData.get(position).getTitle();
                Toast.makeText(mContext, "You clicked: " + itemClicked, Toast.LENGTH_SHORT).show();

                ((Activity) mContext).startActivityForResult(intent, ADD_FAVORITE_REQUEST);
                //mContext.startActivity(intent);

                }
        });

    }

    @Override
    public int getItemCount() {
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

    public static boolean containsMovie(Collection<Movie> favoriteMovies, String id) {
        Log.d(TAG, "MovieAdapter/containsMovie: Favorite movie list size: " + favoriteMovies.size());
        for(Movie movie : favoriteMovies) {
            if(movie != null && movie.getId().equals(id)) {
                Log.d(TAG, "containsMovie: checked as TRUE");
                return true;
            }
        }
        Log.d(TAG, "containsMovie: checked as FALSE");
        return false;
    }

}
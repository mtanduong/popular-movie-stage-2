package com.example.popularmoviesstage1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.models.Movie;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Started");
        getIncomingIntent();
        setTitle(mTitle);
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for Intents");

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("Movie Detail");

        String title = movie.getmTitle();
        String releaseYear = movie.getmYear();
        String overview = movie.getmOverview();
        String releaseDate = movie.getmReleaseDate();
        String userReview = movie.getmUserRating();
        String posterUrl = movie.getmPosterUrl();

        setDetail(title, releaseYear, overview, releaseDate, userReview, posterUrl);
        Log.d(TAG, "getIncomingIntent: Intents completed");
    }

    private void setDetail(String title, String releaseYear, String overview, String releaseDate, String userRating, String posterUrl) {
        Log.d(TAG, "posterUrl: " + posterUrl);
        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(title);
        mTitle = title;

        TextView movieYear = findViewById(R.id.release_year);
        movieYear.setText(releaseYear);

        TextView movieOverview = findViewById(R.id.synopsis);
        movieOverview.setText(overview);

        TextView movieUserRating = findViewById(R.id.user_rating_data);
        movieUserRating.setText(userRating);

        TextView movieReleaseDate = findViewById(R.id.release_date_data);
        movieReleaseDate.setText(releaseDate);

        ImageView image = findViewById(R.id.movie_poster);

        Picasso.get()
                .load(posterUrl)
                .resize(1000, 1500)
                .into(image);
    }
}
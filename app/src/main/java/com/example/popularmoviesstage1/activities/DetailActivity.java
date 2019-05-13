package com.example.popularmoviesstage1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.adapters.TrailerRecyclerViewAdapter;
import com.example.popularmoviesstage1.models.Movie;
import com.example.popularmoviesstage1.models.ReviewDBObject;
import com.example.popularmoviesstage1.models.Video;
import com.example.popularmoviesstage1.models.VideoDBObject;
import com.example.popularmoviesstage1.utils.MovieApiUtils;
import com.example.popularmoviesstage1.utils.MovieService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private MovieService retrofitService = MovieApiUtils.createService();
    private String mTitle;
    private List<Video> trailer;
    private String trailerUrl;
    private RecyclerView recyclerView;

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

        String id = movie.getmId();
        String title = movie.getmTitle();
        String releaseYear = movie.getmYear();
        String overview = movie.getmOverview();
        String releaseDate = movie.getmReleaseDate();
        String userReview = movie.getmUserRating();
        String posterUrl = movie.getmPosterUrl();

        Call<VideoDBObject> callVideos = retrofitService.getVideoMovies(id, MovieApiUtils.API_KEY);
        Call<ReviewDBObject> callReviews = retrofitService.getReviewMovies(id, MovieApiUtils.API_KEY);
        Log.d(TAG, "Post video/review call");

        callVideoApi(callVideos);


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

    private void callVideoApi(Call<VideoDBObject> call) {
        Log.d(TAG, "called callVideoApi");
        call.enqueue(new Callback<VideoDBObject>() {
            @Override
            public void onResponse(Call<VideoDBObject> call, retrofit2.Response<VideoDBObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "callVideoApi server error: " + response.code());
                    //Don't display any video on error
                    return;
                }

                if (response.body().getResults().size() > 0) {
                    parseVideos(response.body());
                }

            }

            @Override
            public void onFailure(Call<VideoDBObject> call, Throwable t) {
                Log.d(TAG, "callVideoApi oncall error: " + t.getMessage());

                //Don't display any video on error
            }
        });
    }

    private void parseVideos(VideoDBObject videoObject) {
        Log.d(TAG, "parseVideos started");

        trailer = videoObject.getResults();

        Log.d(TAG, "videoname: " + trailer.get(0).getmName());
        Log.d(TAG, "videosite: " + trailer.get(0).getmSite());
        Log.d(TAG, "videokey: " + trailer.get(0).getmKey());
        Log.d(TAG, "video link: " + trailer.get(0).getLink());

        trailerUrl = trailer.get(0).getmKey();

        startRecyclerView(trailer);
    }

    private void callReviewApi(Call<ReviewDBObject> call) {

    }

    private void startRecyclerView(List<Video> trailerList) {

        recyclerView = findViewById(R.id.trailer_recycler_view);
        TrailerRecyclerViewAdapter trailerAdapter = new TrailerRecyclerViewAdapter(this, trailerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(trailerAdapter);
    }
}
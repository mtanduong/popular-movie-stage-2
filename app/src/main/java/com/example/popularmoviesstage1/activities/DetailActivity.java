package com.example.popularmoviesstage1.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.adapters.ReviewRecyclerViewAdapter;
import com.example.popularmoviesstage1.adapters.TrailerRecyclerViewAdapter;
import com.example.popularmoviesstage1.models.Movie;
import com.example.popularmoviesstage1.models.MovieView;
import com.example.popularmoviesstage1.models.Review;
import com.example.popularmoviesstage1.models.ReviewDBObject;
import com.example.popularmoviesstage1.models.Video;
import com.example.popularmoviesstage1.models.VideoDBObject;
import com.example.popularmoviesstage1.utils.MovieApiUtils;
import com.example.popularmoviesstage1.utils.MovieService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    public static final String EXTRA_FAVORITE = "EXTRA_FAVORITE";
    private MovieService retrofitService = MovieApiUtils.createService();
    private String mTitle;
    private List<Video> trailer;
    private ImageView favoriteButton;
    private List<Review> review;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Started");
        getIncomingIntent();
        setTitle(mTitle);

        favoriteButton = findViewById(R.id.favorite_button);

        favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(DetailActivity.this, mTitle + " added to Favorites", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
//                movie = intent.getParcelableExtra("Movie Detail");
//                intent.putExtra(EXTRA_FAVORITE, movie);
//                setResult(RESULT_OK, intent);
//                finish();

                Intent intent = getIntent();
                movie = intent.getParcelableExtra("Movie Detail");
                Log.d(TAG, "onClick: INSERTING " + movie.getTitle());

                intent.putExtra(EXTRA_FAVORITE, movie);

                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for Intents");

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Movie Detail");

        String id = movie.getId();
        String title = movie.getTitle();
        String releaseYear = movie.getmYear();
        String overview = movie.getOverview();
        String releaseDate = movie.getReleaseDate();
        String userReview = movie.getUserRating();
        String posterUrl = movie.getPosterUrl();

        Call<VideoDBObject> callVideos = retrofitService.getVideoMovies(id, MovieApiUtils.API_KEY);
        Call<ReviewDBObject> callReviews = retrofitService.getReviewMovies(id, MovieApiUtils.API_KEY);
        Log.d(TAG, "Post video/review call");

        callVideoApi(callVideos);
        callReviewApi(callReviews);

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

        startVideoRecyclerView(trailer);
    }

    private void callReviewApi(Call<ReviewDBObject> call) {
        Log.d(TAG, "called callReviewApi");
        call.enqueue(new Callback<ReviewDBObject>() {
            @Override
            public void onResponse(Call<ReviewDBObject> call, retrofit2.Response<ReviewDBObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "callReviewApi server error: " + response.code());
                    //Don't display any review on error
                    return;
                }

                if (response.body().getResults().size() > 0) {
                    parseReviews(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReviewDBObject> call, Throwable t) {

            }
        });
    }

    private void parseReviews(ReviewDBObject reviewObject) {
        Log.d(TAG, "parseReviews stated");

        review = reviewObject.getResults();

        startReviewRecyclerView(review);
    }

    private void startVideoRecyclerView(List<Video> trailerList) {
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.trailer_recycler_view);
        TrailerRecyclerViewAdapter trailerAdapter = new TrailerRecyclerViewAdapter(this, trailerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(trailerAdapter);
    }

    private void startReviewRecyclerView(List<Review> reviewList) {
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.review_recycler_view);
        ReviewRecyclerViewAdapter reviewAdapter =new ReviewRecyclerViewAdapter(this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(reviewAdapter);
    }
}
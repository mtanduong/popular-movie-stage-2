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
    public static final String EXTRA_MODIFY = "EXTRA_MODIFY";
    private MovieService retrofitService = MovieApiUtils.createService();
    private String mTitle;
    private List<Video> trailer;
    private ImageView favoriteButton;
    private ImageView unfavoriteButton;
    private List<Review> review;
    private Movie movie;
    private boolean favorite;
    private String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        favoriteButton = findViewById(R.id.favorite_button);
        unfavoriteButton = findViewById(R.id.unfavorite_button);

        Log.d(TAG, "onCreate: Started");
        getIncomingIntent();
        setTitle(mTitle);



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
                intent.putExtra(EXTRA_MODIFY, true);

                setResult(RESULT_OK, intent);
                finish();

            }
        });

        unfavoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                movie = intent.getParcelableExtra("Movie Detail");
                Log.d(TAG, "onClick: REMOVING " + movie.getTitle());

                intent.putExtra(EXTRA_FAVORITE, movie);
                intent.putExtra(EXTRA_MODIFY, false);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getIncomingIntent() {
        Log.d(TAG, "DetailActivity/getIncomingIntent: Checking incoming Intents from MovieAdapter");

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Movie Detail");
        favorite = intent.getBooleanExtra("Favorite", false);
        Log.d(TAG, "DetailActivity/getIncomingIntent: Intent Favorite flag received as " + favorite);

        if (!favorite) {
            Log.d(TAG, "DetailActivity/getIncomingIntent/button: Set button as Favorite");
            unfavoriteButton.setVisibility(View.GONE);
            favoriteButton.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "DetailActivity/getIncomingIntent/button: Set button as Unfavorite");
            unfavoriteButton.setVisibility(View.VISIBLE);
            favoriteButton.setVisibility(View.GONE);
        }

        String id = movie.getId();
        String title = movie.getTitle();
        String releaseYear = movie.getmYear();
        String overview = movie.getOverview();
        String releaseDate = movie.getReleaseDate();

        String[] parts = releaseDate.split("-");
        releaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];

        String userReview = movie.getUserRating();
        String posterUrl = movie.getPosterUrl();

        Call<VideoDBObject> callVideos = retrofitService.getVideoMovies(id, MovieApiUtils.API_KEY);
        Call<ReviewDBObject> callReviews = retrofitService.getReviewMovies(id, MovieApiUtils.API_KEY);

        callVideoApi(callVideos);
        callReviewApi(callReviews);

        setDetail(title, releaseYear, overview, releaseDate, userReview, posterUrl);
    }

    private void setDetail(String title, String releaseYear, String overview, String releaseDate, String userRating, String posterUrl) {
        Log.d(TAG, "DetailActivity/setDetail: title: " + title);
        Log.d(TAG, "DetailActivity/setDetail: releaseYear: " + releaseYear);
        Log.d(TAG, "DetailActivity/setDetail: overview: " + overview);
        Log.d(TAG, "DetailActivity/setDetail: releaseDate: " + releaseDate);
        Log.d(TAG, "DetailActivity/setDetail: userRating: " + userRating);
        Log.d(TAG, "DetailActivity/setDetail: posterUrl: " + posterUrl);

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
                .load("https://image.tmdb.org/t/p/w300" + posterUrl)
                .resize(1000, 1500)
                .into(image);
    }

    private void callVideoApi(Call<VideoDBObject> call) {
        Log.d(TAG, "DetailActivity/callVideoApi: Called");
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
        Log.d(TAG, "DetailActivity/parseVideos: Called");

        trailer = videoObject.getResults();

        Log.d(TAG, "DetailActivity/parseVideos: videoName: " + trailer.get(0).getmName());
        Log.d(TAG, "DetailActivity/parseVideos: videoSite: " + trailer.get(0).getmSite());
        Log.d(TAG, "DetailActivity/parseVideos: videoKey: " + trailer.get(0).getmKey());
        Log.d(TAG, "DetailActivity/parseVideos: videoLink: " + trailer.get(0).getLink());

        startVideoRecyclerView(trailer);
    }

    private void callReviewApi(Call<ReviewDBObject> call) {
        Log.d(TAG, "DetailActivity/callReviewApi: Called");
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
        Log.d(TAG, "DetailActivity/parseReviews: Called");

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
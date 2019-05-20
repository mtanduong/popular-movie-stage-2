package com.example.popularmoviesstage1.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesstage1.R;
import com.example.popularmoviesstage1.adapters.MovieRecyclerViewAdapter;
import com.example.popularmoviesstage1.models.MovieDBObject;
import com.example.popularmoviesstage1.models.Movie;
import com.example.popularmoviesstage1.models.MovieView;
import com.example.popularmoviesstage1.utils.MovieApiUtils;
import com.example.popularmoviesstage1.utils.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int ADD_FAVORITE_REQUEST = 1;

    private MovieService retrofitService = MovieApiUtils.createService();
    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private ImageView noServer;
    private TextView errorText;
    private Button retryButton;
    private String sort;
    private MovieView movieView;

    public static final String SAVED_PREFS = "savedPrefs";
    public static final String SORT = "sort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noConnection = findViewById(R.id.no_network_iv);
        noConnection.setVisibility(View.GONE);

        noServer = findViewById(R.id.no_server_iv);
        noServer.setVisibility(View.GONE);

        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        movieList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


//        Movie movie = new Movie("297761", "testtitle", "testoverview", "2016-08-03", "5.91", "testurl");
        movieView = ViewModelProviders.of(this).get(MovieView.class);
//        movieView.insert(movie);
        //final MovieRecyclerViewAdapter adapter = new MovieRecyclerViewAdapter();

        //Probably don't need onChanged.. if we're going to call DB when favorite option is selected
//         movieView = ViewModelProviders.of(this).get(MovieView.class);
//        movieView.getAllMovies().observe(this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(List<Movie> movies) {
//                Toast.makeText(MainActivity.this, "onChange triggered", Toast.LENGTH_SHORT).show();
//                //startRecyclerView(movies);
//                //recyclerView.setAdapter(adapter);
//            }
//        });

        retryButton = findViewById(R.id.reset_button);
        retryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "retry attempt");
                Call<MovieDBObject> callPopularMovies = retrofitService.getPopularMovies(MovieApiUtils.API_KEY);
                callApi(callPopularMovies);
            }
        });
        retryButton.setVisibility(View.GONE);

        loadPref();
        switch (sort) {
            case "Most Popular":
                Call<MovieDBObject> callPopularMovies = retrofitService.getPopularMovies(MovieApiUtils.API_KEY);
                Log.d(TAG, "retrofit getPop string: " + callPopularMovies.toString());
                callApi(callPopularMovies);
                break;
            case "Top Rated":
                Call<MovieDBObject> callRatedMovies = retrofitService.getTopRatedMovies(MovieApiUtils.API_KEY);
                Log.d(TAG, "retrofit getPop string: " + callRatedMovies.toString());
                callApi(callRatedMovies);
                break;
            case "Favorites":
                //call room db, return list of movies
                //startRecyclerView(MovieList);
                //to reload onCreate with previous last option selected
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: TRIGGERED ");
        if (requestCode == ADD_FAVORITE_REQUEST && resultCode == RESULT_OK) {
            Movie movie = data.getParcelableExtra(DetailActivity.EXTRA_FAVORITE);

            movieView.insert(movie);

            Log.d(TAG, "Successfully inserted into DB: " + movie.getTitle());

            Toast.makeText(this, movie.getTitle() + " saved to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Movie NOT saved to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular_sort_option:
                Toast.makeText(this, "Popular Sort selected", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "item title: " + item.getTitle());
                Call<MovieDBObject> callPopularMovies = retrofitService.getPopularMovies(MovieApiUtils.API_KEY);
                Log.d(TAG, "retrofit getPop string: " + callPopularMovies.toString());
                callApi(callPopularMovies);
                savePref(item.getTitle().toString());
                return true;

            case R.id.rating_sort_option:
                Toast.makeText(this, "Rated Sort selected", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "item title: " + item.getTitle());
                Call<MovieDBObject> callRatedMovies = retrofitService.getTopRatedMovies(MovieApiUtils.API_KEY);
                Log.d(TAG, "retrofit getPop string: " + callRatedMovies.toString());
                callApi(callRatedMovies);
                savePref(item.getTitle().toString());
                return true;

            case R.id.favorite_sort_option:
                Toast.makeText(this, "Favorite Sort selected", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "item title: " + item.getTitle());

                //call room db, return list of movies
                //movieList = movieView.getAllMovies();
                //startRecyclerView(movieView.getAllMovies());

                savePref(item.getTitle().toString());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRecyclerView(List<Movie> movieList) {

        MovieRecyclerViewAdapter movieAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieAdapter);
    }

    private void callApi(Call<MovieDBObject> call) {

        Log.d(TAG, "called callApi");
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MovieDBObject>() {
            @Override
            public void onResponse(Call<MovieDBObject> call, retrofit2.Response<MovieDBObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "callApi server error: " + response.code());
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.server_error);
                    progressBar.setVisibility(View.GONE);
                    noServer.setVisibility(View.VISIBLE);
                    return;
                }
                Log.d(TAG, "pre parseMovies");
                progressBar.setVisibility(View.GONE);
                parseMovies(response.body());
                Log.d(TAG, "post parseMovies");
            }

            @Override
            public void onFailure(Call<MovieDBObject> call, Throwable t) {
                Log.d(TAG, "callApi oncall error: " + t.getMessage());

                errorText.setText("");
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (t instanceof IOException) {
                    errorText.setText(R.string.network_error);
                    noConnection.setVisibility(View.VISIBLE);
                } else {
                    errorText.setText(R.string.parse_error);
                }
            }
        });

    }

    private void parseMovies(MovieDBObject movieObject) {
        Log.d(TAG, "parseMovies started");
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);
        noServer.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);

        movieList = movieObject.getMovieList();

        Log.d(TAG, "moviename: " + movieList.get(0).getTitle());
        Log.d(TAG, "moviename: " + movieList.get(0).getThumbnailImgUrl());
        Log.d(TAG, "moviename: " + movieList.get(0).getPosterUrl());
        Log.d(TAG, "moviename: " + movieList.get(0).getOverview());
        Log.d(TAG, "moviename: " + movieList.get(0).getReleaseDate());
        Log.d(TAG, "moviename: " + movieList.get(0).getUserRating());

        startRecyclerView(movieList);
        progressBar.setVisibility(View.GONE);
    }

    private void savePref(String sort) {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SORT, sort);
        editor.apply();
        Log.d(TAG, "Saving pref: " + sort);
    }

    private void loadPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVED_PREFS, MODE_PRIVATE);
        sort = sharedPreferences.getString(SORT, "Most Popular");
    }
}
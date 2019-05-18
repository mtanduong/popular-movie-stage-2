package com.example.popularmoviesstage1.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.popularmoviesstage1.utils.MovieApiUtils;
import com.example.popularmoviesstage1.utils.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MovieService retrofitService = MovieApiUtils.createService();
    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private ImageView noServer;
    private TextView errorText;
    private Button retryButton;
    private String sort;

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
        }
    }

    private void startRecyclerView(List<Movie> movieList) {

        MovieRecyclerViewAdapter movieAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(movieAdapter);
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
        }
        return super.onOptionsItemSelected(item);
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
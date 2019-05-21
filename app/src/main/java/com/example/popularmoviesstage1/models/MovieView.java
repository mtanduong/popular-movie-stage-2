package com.example.popularmoviesstage1.models;

import android.app.Application;

import com.example.popularmoviesstage1.database.MovieRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MovieView extends AndroidViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> allMovies;

    public MovieView(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }

    public void deleteAllMovies() {
        repository.deleteAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }
}

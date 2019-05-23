package com.example.popularmoviesstage1.database;

import android.app.Application;
import android.os.AsyncTask;

import com.example.popularmoviesstage1.models.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieRepository {
    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;

    public MovieRepository(Application application) {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDao = database.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public void insert(Movie movie) {
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void update(Movie movie) {
        new UpdateMovieAsyncTask(movieDao).execute(movie);
    }

    public void delete(Movie movie) {
        new DeleteMovieAsyncTask(movieDao).execute(movie);
    }

    public void deleteAllMovies() {
        new DeleteAllMoviesAsyncTask(movieDao).execute();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    private static class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        private InsertMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        private UpdateMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.update(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        private DeleteMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteAllMoviesAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDao movieDao;

        private DeleteAllMoviesAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDao.deleteAllMovies();
            return null;
        }
    }
}

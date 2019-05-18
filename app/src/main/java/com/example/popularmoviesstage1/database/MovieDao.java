package com.example.popularmoviesstage1.database;

import com.example.popularmoviesstage1.models.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieDao {

    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM favorites_table ORDER BY userRating DESC")
    LiveData<List<Movie>> getAllMovies();

}

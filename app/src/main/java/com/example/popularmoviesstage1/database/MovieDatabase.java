package com.example.popularmoviesstage1.database;

import android.content.Context;

import com.example.popularmoviesstage1.models.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;

    public abstract MovieDao movieDao();

    public static synchronized MovieDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, "favorites_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

package com.google.app.movieapp1.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY added_at DESC")
    LiveData<List<MovieEntry>> loadAllFavMovies();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Delete
    void deleteMovie(MovieEntry movieEntry);

    @Query("SELECT * FROM movie WHERE id = :id")
    MovieEntry findMovieById(int id);
}

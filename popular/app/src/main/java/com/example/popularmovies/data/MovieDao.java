package com.example.popularmovies.data;



import android.database.Cursor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.*;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> loadAllMovies();

    @Insert
    long insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM movies WHERE id = :movieId")
    int deleteMovie(String movieId);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> loadMovieById(String id);

    @Query("SELECT * FROM movies")
    Cursor retrieveAll();
}

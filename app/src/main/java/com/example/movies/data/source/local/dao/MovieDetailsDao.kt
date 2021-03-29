package com.example.movies.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.model.MovieDetails

@Dao
interface MovieDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDetails(details: MovieDetails)

    @Query("SELECT * FROM movie_details WHERE id =:id")
    fun getMovieDetails(id: Int): LiveData<MovieDetails?>
}
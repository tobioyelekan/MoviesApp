package com.example.movies.data.repo

import androidx.lifecycle.LiveData
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails

interface MovieRepository {

    suspend fun getMovies()

    fun observeMovies(): LiveData<List<Movie>>

    suspend fun getMovieDetails(id: Int)

    fun observeMovieDetails(id: Int): LiveData<MovieDetails?>

}
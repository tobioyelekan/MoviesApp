package com.example.movies.data.source.local

import androidx.lifecycle.LiveData
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails

interface MoviesLocalDataSource {

    suspend fun saveMovies(movies: List<Movie>)

    suspend fun saveMovieDetails(details: MovieDetails)

    fun observeMovies(): LiveData<List<Movie>>

    fun observeMovieDetails(id: Int): LiveData<MovieDetails?>
}
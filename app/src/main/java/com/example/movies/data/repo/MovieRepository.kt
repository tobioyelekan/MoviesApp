package com.example.movies.data.repo

import androidx.lifecycle.LiveData
import com.example.movies.data.helper.Resource
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails

interface MovieRepository {

    suspend fun saveMovies(movies: List<Movie>)

    suspend fun saveMovieDetails(details: MovieDetails)

    fun fetchMovies(): LiveData<Resource<Unit>>

    fun fetchMovieDetails(id: Int): LiveData<Resource<Unit>>

    fun observeMovies(): LiveData<List<Movie>>

    fun observeMovieDetails(id: Int): LiveData<MovieDetails?>

}
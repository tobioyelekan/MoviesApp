package com.example.movies.data.source.remote

import com.example.movies.data.helper.Resource
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.MovieResponse

interface MovieRemoteDataSource {
    suspend fun getPopularMovies(): Resource<MovieResponse>

    suspend fun getMovieDetails(id: Int): Resource<MovieDetails>
}
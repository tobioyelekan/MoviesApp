package com.example.movies.data.source.remote

import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetails
}
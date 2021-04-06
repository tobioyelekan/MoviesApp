package com.example.movies.data.repo

import com.example.movies.data.helper.Resource.*
import com.example.movies.data.source.local.MoviesLocalDataSource
import com.example.movies.data.source.remote.MovieRemoteDataSource

class DefaultRepository(
    private val localDataSource: MoviesLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun getMovies() {
        val response = remoteDataSource.getPopularMovies()
        when (response.status) {
            Status.SUCCESS -> {
                response.data?.let {
                    localDataSource.saveMovies(it.results)
                }
            }
            else -> {
                throw Throwable(response.message)
            }
        }
    }

    override suspend fun getMovieDetails(id: Int) {
        val response = remoteDataSource.getMovieDetails(id)
        when (response.status) {
            Status.SUCCESS -> {
                response.data?.let {
                    localDataSource.saveMovieDetails(it)
                }
            }
            else -> {
                throw Throwable(response.message)
            }
        }
    }

    override fun observeMovies() = localDataSource.observeMovies()

    override fun observeMovieDetails(id: Int) = localDataSource.observeMovieDetails(id)

}
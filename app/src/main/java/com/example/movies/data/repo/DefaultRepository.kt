package com.example.movies.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.movies.data.helper.Resource
import com.example.movies.data.helper.Resource.*
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.source.local.MoviesLocalDataSource
import com.example.movies.data.source.remote.MovieRemoteDataSource

class DefaultRepository(
    private val localDataSource: MoviesLocalDataSource,
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override fun fetchMovies(): LiveData<Resource<Unit>> =
        liveData {
            val response = remoteDataSource.getPopularMovies()
            when (response.status) {
                Status.LOADING -> {
                    emit(Resource.loading())
                }
                Status.SUCCESS -> {
                    response.data?.let {
                        saveMovies(it.results)
                    }
                    emit(Resource.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resource.error(response.message!!, null))
                }
            }
        }

    override fun fetchMovieDetails(id: Int): LiveData<Resource<Unit>> =
        liveData {
            val response = remoteDataSource.getMovieDetails(id)
            when (response.status) {
                Status.LOADING -> {
                    emit(Resource.loading())
                }
                Status.SUCCESS -> {
                    response.data?.let {
                        saveMovieDetails(it)
                    }
                    emit(Resource.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resource.error(response.message!!, null))
                }
            }
        }

    override suspend fun saveMovies(movies: List<Movie>) {
        localDataSource.saveMovies(movies)
    }

    override suspend fun saveMovieDetails(details: MovieDetails) {
        localDataSource.saveMovieDetails(details)
    }

    override fun observeMovies() = localDataSource.observeMovies()

    override fun observeMovieDetails(id: Int) = localDataSource.observeMovieDetails(id)

}
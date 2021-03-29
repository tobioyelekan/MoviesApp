package com.example.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.movies.TestObjectUtil
import com.example.movies.data.helper.Resource
import com.example.movies.data.helper.Resource.Status
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.repo.MovieRepository

class FakeRepository : MovieRepository {
    private var shouldReturnError = false

    private val movies = mutableListOf<Movie>()
    private var movieDetails: MovieDetails? = null

    private val observeMovies = MutableLiveData<List<Movie>>()
    private val observeMovieDetail = MutableLiveData<MovieDetails>()

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun fetchMovies(): LiveData<Resource<Unit>> {
        return liveData {
            val response = if (shouldReturnError) {
                Resource.error("error occurred", null)
            } else {
                Resource.success(TestObjectUtil.movies)
            }

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        saveMovies(it)
                    }
                    emit(Resource.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resource.error(response.message!!, null))
                }
                else -> {
                }
            }
        }
    }

    override suspend fun saveMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        refreshData()
    }

    private fun refreshData() {
        observeMovies.value = movies
        observeMovieDetail.value = movieDetails
    }

    override suspend fun saveMovieDetails(details: MovieDetails) {
        movieDetails = details
        refreshData()
    }

    override fun fetchMovieDetails(id: Int): LiveData<Resource<Unit>> {
        return liveData {
            val response = if (shouldReturnError) {
                Resource.error("error occurred", null)
            } else {
                Resource.success(TestObjectUtil.movieDetails.find { it.id == id })
            }

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        saveMovieDetails(it)
                    }
                    emit(Resource.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resource.error(response.message!!, null))
                }
                else -> {
                }
            }
        }
    }

    override fun observeMovies(): LiveData<List<Movie>> {
        return observeMovies
    }

    override fun observeMovieDetails(id: Int): LiveData<MovieDetails?> {
        observeMovieDetail.value = TestObjectUtil.movieDetails.find { it.id == id }
        return observeMovieDetail
    }
}
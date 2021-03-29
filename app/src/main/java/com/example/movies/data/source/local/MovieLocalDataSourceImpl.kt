package com.example.movies.data.source.local

import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.source.local.dao.MovieDao
import com.example.movies.data.source.local.dao.MovieDetailsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val movieDetailsDao: MovieDetailsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesLocalDataSource {

    override suspend fun saveMovies(movies: List<Movie>) = withContext(ioDispatcher) {
        movieDao.saveMovies(movies)
    }

    override suspend fun saveMovieDetails(details: MovieDetails) = withContext(ioDispatcher) {
        movieDetailsDao.saveMovieDetails(details)
    }

    override fun observeMovies() = movieDao.getMovies()

    override fun observeMovieDetails(id: Int) = movieDetailsDao.getMovieDetails(id)
}
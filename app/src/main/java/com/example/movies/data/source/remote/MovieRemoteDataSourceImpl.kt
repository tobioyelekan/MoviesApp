package com.example.movies.data.source.remote

import com.example.movies.data.helper.Resource
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.MovieResponse
import com.example.movies.util.parseError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRemoteDataSource {
    override suspend fun getPopularMovies(): Resource<MovieResponse> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = movieService.getPopularMovies()
                Resource.success(response)
            } catch (ex: Throwable) {
                ex.printStackTrace()
                Resource.error(resolveError(ex), null)
            }
        }

    override suspend fun getMovieDetails(id: Int): Resource<MovieDetails> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = movieService.getMovieDetails(id)
                Resource.success(response)
            } catch (ex: Throwable) {
                ex.printStackTrace()
                Resource.error(resolveError(ex), null)
            }
        }

    private fun resolveError(throwable: Throwable): String {
        val error = if (throwable is HttpException) {
            throwable.response()?.errorBody()?.string()?.parseError()
        } else {
            null
        }

        return error ?: throwable.message ?: throwable.localizedMessage
        ?: "Something went wrong"
    }
}
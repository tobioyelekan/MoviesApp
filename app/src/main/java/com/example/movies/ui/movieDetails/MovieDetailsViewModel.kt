package com.example.movies.ui.movieDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.movies.data.helper.Resource
import com.example.movies.data.repo.MovieRepository

class MovieDetailsViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieId = MutableLiveData<Int>()

    val movieDetails = _movieId.switchMap {
        liveData(context = viewModelScope.coroutineContext) {
            emit(Resource.loading())
            try {
                repository.getMovieDetails(it)
                emit(Resource.success(Unit))
            } catch (e: Throwable) {
                emit(Resource.error(e.message!!))
            }
        }
    }

    fun getMovieDetails(id: Int) {
        _movieId.value = id
    }

    fun observeMovieDetails(id: Int) = repository.observeMovieDetails(id)
}
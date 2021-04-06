package com.example.movies.ui.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.movies.data.helper.Resource
import com.example.movies.data.repo.MovieRepository

class MoviesViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    val movieLoader = liveData(context = viewModelScope.coroutineContext) {
        emit(Resource.loading())
        try {
            repository.getMovies()
            emit(Resource.success(Unit))
        } catch (e: Throwable) {
            emit(Resource.error(message = e.message!!))
        }
    }

    val movies = repository.observeMovies()
}
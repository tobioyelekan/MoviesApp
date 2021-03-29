package com.example.movies.ui.movieDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.movies.data.repo.MovieRepository

class MovieDetailsViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieId = MutableLiveData<Int>()

    val movieDetails = _movieId.switchMap {
        repository.fetchMovieDetails(it)
    }

    fun getMovieDetails(id: Int) {
        _movieId.value = id
    }

    fun observeMovieDetails(id: Int) = repository.observeMovieDetails(id)
}
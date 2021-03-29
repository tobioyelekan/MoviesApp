package com.example.movies.ui.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.movies.data.repo.MovieRepository

class MoviesViewModel @ViewModelInject constructor(
    repository: MovieRepository
) : ViewModel() {

    val fetchMovies = repository.fetchMovies()

    val movies = repository.observeMovies()
}
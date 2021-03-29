package com.example.movies

import com.example.movies.data.model.Genre
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.MovieResponse

object TestObjectUtil {
    val movies = listOf(
        Movie(1, "Superman", "1999-02-01", 9.9f, "path.png"),
        Movie(2, "WonderWoman", "1999-02-01", 8.0f, "path.png"),
        Movie(3, "Batman", "1999-02-01", 7.5f, "path.png")
    )

    val movieDetails = listOf(
        MovieDetails(
            1, "Superman", "1999-02-01", 9.9f, "path.png", "Majestic", "released", 1000L, 1000L,
            listOf(Genre(1, "suspenseful"))
        ),
        MovieDetails(
            2, "WonderWoman", "1999-02-01", 8.0f, "path.png", "Majestic", "released", 1000L, 1000L,
            listOf(Genre(1, "suspenseful"))
        ),
        MovieDetails(
            3, "Batman", "1999-02-01", 7.5f, "path.png", "Majestic", "released", 1000L, 1000L,
            listOf(Genre(1, "suspenseful"))
        )
    )


    val movieResponse = MovieResponse(results = movies)
}
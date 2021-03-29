package com.example.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val vote: Float,
    @SerializedName("poster_path")
    val imgPath: String
)

data class MovieResponse(
    val results: List<Movie>
)
package com.example.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_details")
data class MovieDetails(
    @PrimaryKey
    val id: Int,
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val vote: Float,
    @SerializedName("backdrop_path")
    val imgPath: String,
    val overview: String,
    val status: String,
    val budget: Long,
    val revenue: Long,
    val genres: List<Genre>
)


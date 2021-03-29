package com.example.movies.data.source.local

import android.content.Context
import androidx.room.*
import com.example.movies.data.model.Genre
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.source.local.dao.MovieDao
import com.example.movies.data.source.local.dao.MovieDetailsDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(
    entities = [Movie::class, MovieDetails::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailsDao

    companion object {
        private var instance: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            if (instance == null) {
                synchronized(MoviesDatabase::class.java) {}
                instance =
                    Room.databaseBuilder(context, MoviesDatabase::class.java, "AppDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
            }

            return instance!!
        }
    }
}

class Converter {
    @TypeConverter
    fun fromGenreList(list: List<Genre>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toGenreList(list: String): List<Genre> {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(list, type)
    }
}
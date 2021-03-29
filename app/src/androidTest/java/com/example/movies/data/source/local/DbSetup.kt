package com.example.movies.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.movies.data.source.local.dao.MovieDao
import com.example.movies.data.source.local.dao.MovieDetailsDao
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DbSetup {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: MoviesDatabase
    protected lateinit var moviesDao: MovieDao
    protected lateinit var movieDetailsDao: MovieDetailsDao

    @Before
    fun init() {
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            MoviesDatabase::class.java
        ).build()

        moviesDao = db.moviesDao()
        movieDetailsDao = db.movieDetailDao()
    }

    @After
    fun closeDb() {
        db.close()
    }
}
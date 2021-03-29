package com.example.movies.data.source.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.movies.TestObjectUtil
import com.example.movies.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest : DbSetup() {

    @Test
    fun insertAndRetrieve() = runBlocking {
        val movies = TestObjectUtil.movies
        moviesDao.saveMovies(movies)

        val response = moviesDao.getMovies().getOrAwaitValue()
        assertThat(response.size, `is`(movies.size))
        assertThat(response, `is`(movies))
    }
}
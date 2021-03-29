package com.example.movies.ui.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.MainCoroutineRule
import com.example.movies.TestObjectUtil
import com.example.movies.data.FakeRepository
import com.example.movies.data.helper.Resource
import com.example.movies.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private val repository = FakeRepository()

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun init() {
        viewModel = MoviesViewModel(repository)
    }

    @Test
    fun `assert that call to network emits success`() {
        mainCoroutine.runBlockingTest {
            val status = viewModel.fetchMovies.getOrAwaitValue()
            assertThat(status, `is`(Resource.success(Unit)))
        }
    }

    @Test
    fun `assert that error response is received when error occurs while calling network`() {
        mainCoroutine.runBlockingTest {
            repository.setShouldReturnError(true)

            val status = viewModel.fetchMovies.getOrAwaitValue()
            assertThat(status, `is`(Resource.error("error occurred")))
        }
    }

    @Test
    fun `assert that live data emits movies stored in local data after network call to server`() {
        mainCoroutine.runBlockingTest {
            val status = viewModel.fetchMovies.getOrAwaitValue()
            assertThat(status, `is`(Resource.success(Unit)))

            val movies = viewModel.movies.getOrAwaitValue()
            assertThat(movies, `is`(TestObjectUtil.movies))
        }
    }

}
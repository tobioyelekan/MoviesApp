package com.example.movies.ui.movieDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.MainCoroutineRule
import com.example.movies.TestObjectUtil
import com.example.movies.data.FakeRepository
import com.example.movies.data.helper.Resource
import com.example.movies.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDetailsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private val repository = FakeRepository()

    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun init() {
        viewModel = MovieDetailsViewModel(repository)
    }

    @Test
    fun `assert that call to network emits success`() {
        mainCoroutine.runBlockingTest {
            val testMovieDetails = TestObjectUtil.movieDetails[0]
            val testId = testMovieDetails.id
            viewModel.getMovieDetails(testId)

            val status = viewModel.movieDetails.getOrAwaitValue()
            assertThat(status, `is`(Resource.success(Unit)))
        }
    }

    @Test
    fun `assert that when error occurs live data emits error with appropriate error message`() {
        mainCoroutine.runBlockingTest {
            repository.setShouldReturnError(true)

            val testMovieDetails = TestObjectUtil.movieDetails[0]
            val testId = testMovieDetails.id
            viewModel.getMovieDetails(testId)

            val status = viewModel.movieDetails.getOrAwaitValue()
            assertThat(status, `is`(Resource.error("error occurred")))
        }
    }

    @Test
    fun `assert that observe movie details emits data after save in local storage`() {
        mainCoroutine.runBlockingTest {
            val testMovieDetails = TestObjectUtil.movieDetails[0]
            val testId = testMovieDetails.id
            viewModel.getMovieDetails(testId)

            val status = viewModel.movieDetails.getOrAwaitValue()
            assertThat(status, `is`(Resource.success(Unit)))

            val data = viewModel.observeMovieDetails(testId).getOrAwaitValue()
            assertThat(data, `is`(testMovieDetails))
        }
    }

}
package com.example.movies.data

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.movies.MainCoroutineRule
import com.example.movies.TestObjectUtil
import com.example.movies.data.helper.Resource
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.repo.DefaultRepository
import com.example.movies.data.source.local.MoviesLocalDataSource
import com.example.movies.data.source.remote.MovieRemoteDataSource
import com.example.movies.getOrAwaitValue
import com.nhaarman.mockitokotlin2.argumentCaptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RepositoryTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: DefaultRepository
    private val remoteDataSource = mock(MovieRemoteDataSource::class.java)
    private val localDataSource = mock(MoviesLocalDataSource::class.java)

    @Before
    fun setup() {
        repository = DefaultRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `assert that fetchMovie call to network is successful`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getPopularMovies()).thenReturn(Resource.success(TestObjectUtil.movieResponse))
            val response = repository.fetchMovies().getOrAwaitValue()

            verify(remoteDataSource).getPopularMovies()

            assertThat(response, `is`(Resource.success(Unit)))
        }

    @Test
    fun `assert that when network call fetchMovie fails, it returns error message`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getPopularMovies()).thenReturn(Resource.error("error occurred"))
            val response = repository.fetchMovies().getOrAwaitValue()

            verify(remoteDataSource).getPopularMovies()
            assertThat(response, `is`(Resource.error("error occurred")))
            verifyNoMoreInteractions(remoteDataSource, localDataSource)
        }

    @Test
    fun `assert that fetchMovieDetails call to network is successful`() =
        mainCoroutineRule.runBlockingTest {
            val testMovieDetails = TestObjectUtil.movieDetails[0]
            val testId = testMovieDetails.id

            `when`(remoteDataSource.getMovieDetails(testId)).thenReturn(
                Resource.success(
                    testMovieDetails
                )
            )
            val response = repository.fetchMovieDetails(testId).getOrAwaitValue()

            verify(remoteDataSource).getMovieDetails(testId)

            assertThat(response, `is`(Resource.success(Unit)))
        }

    @Test
    fun `assert that when network call fetchMovieDetails fails, it returns error message`() =
        mainCoroutineRule.runBlockingTest {
            val testMovieDetails = TestObjectUtil.movieDetails[0]
            val testId = testMovieDetails.id

            `when`(remoteDataSource.getMovieDetails(testId)).thenReturn(Resource.error("error occurred"))
            val response = repository.fetchMovieDetails(testId).getOrAwaitValue()

            verify(remoteDataSource).getMovieDetails(testId)

            assertThat(response, `is`(Resource.error("error occurred")))
            verifyNoMoreInteractions(remoteDataSource, localDataSource)
        }

    @Test
    fun `assert that call to fetchMovie persist data`() = mainCoroutineRule.runBlockingTest {
        `when`(remoteDataSource.getPopularMovies()).thenReturn(Resource.success(TestObjectUtil.movieResponse))

        val response = repository.fetchMovies().getOrAwaitValue()

        assertThat(response, `is`(Resource.success(Unit)))
        verify(remoteDataSource).getPopularMovies()
        verify(localDataSource).saveMovies(TestObjectUtil.movies)
    }

    @Test
    fun `assert that repository retrieves movies`() {
        val data = MutableLiveData<List<Movie>>()
        data.value = TestObjectUtil.movies

        `when`(localDataSource.observeMovies()).thenReturn(data)
        val response = repository.observeMovies().getOrAwaitValue()
        assertThat(response, `is`(TestObjectUtil.movies))
    }

    @Test
    fun `assert that repository saves movies to local storage`() =
        mainCoroutineRule.runBlockingTest {
            val argumentCaptor = argumentCaptor<List<Movie>>()

            repository.saveMovies(TestObjectUtil.movies)
            verify(localDataSource).saveMovies(argumentCaptor.capture())

            val movies = argumentCaptor.firstValue
            assertThat(movies, `is`(TestObjectUtil.movies))
        }

    @Test
    fun `assert that repository retrieves movie details`() {
        val testMovieDetails = TestObjectUtil.movieDetails[0]
        val testId = testMovieDetails.id

        val data = MutableLiveData<MovieDetails>()
        data.value = testMovieDetails

        `when`(localDataSource.observeMovieDetails(testId)).thenReturn(data)
        val response = repository.observeMovieDetails(testId).getOrAwaitValue()
        verify(localDataSource).observeMovieDetails(testId)
        assertThat(response, `is`(notNullValue()))
        assertThat(response, `is`(testMovieDetails))
    }

    @Test
    fun `assert that repository saves movie details to local storage`() =
        mainCoroutineRule.runBlockingTest {
            val testMovieDetails = TestObjectUtil.movieDetails[0]
            val argumentCaptor = argumentCaptor<MovieDetails>()

            repository.saveMovieDetails(testMovieDetails)
            verify(localDataSource).saveMovieDetails(argumentCaptor.capture())

            val movieDetails = argumentCaptor.firstValue
            assertThat(movieDetails, `is`(testMovieDetails))
        }
}
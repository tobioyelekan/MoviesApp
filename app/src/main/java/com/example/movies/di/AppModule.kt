package com.example.movies.di

import android.content.Context
import com.example.movies.BuildConfig
import com.example.movies.BuildConfig.API_KEY
import com.example.movies.BuildConfig.BASE_URL
import com.example.movies.data.repo.DefaultRepository
import com.example.movies.data.repo.MovieRepository
import com.example.movies.data.source.local.MovieLocalDataSourceImpl
import com.example.movies.data.source.local.MoviesDatabase
import com.example.movies.data.source.local.MoviesLocalDataSource
import com.example.movies.data.source.local.dao.MovieDao
import com.example.movies.data.source.local.dao.MovieDetailsDao
import com.example.movies.data.source.remote.MovieRemoteDataSource
import com.example.movies.data.source.remote.MovieRemoteDataSourceImpl
import com.example.movies.data.source.remote.MovieService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun providesMovieDao(@ApplicationContext context: Context): MovieDao {
        return MoviesDatabase.getDatabase(context).moviesDao()
    }

    @Provides
    @Singleton
    fun providesMovieDetailsDao(@ApplicationContext context: Context): MovieDetailsDao {
        return MoviesDatabase.getDatabase(context).movieDetailDao()
    }

    @Provides
    @Singleton
    fun providesMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun providesRepository(
        localDataSource: MoviesLocalDataSource,
        remoteDataSource: MovieRemoteDataSource
    ): MovieRepository {
        return DefaultRepository(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun providesRemoteDataSource(service: MovieService): MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun providesLocalDataSource(
        movieDao: MovieDao,
        movieDetailsDao: MovieDetailsDao
    ): MoviesLocalDataSource {
        return MovieLocalDataSourceImpl(movieDao, movieDetailsDao)
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val httpUrl = request.url

                    val newUrl = httpUrl.newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build()

                    val requestBuilder = request.newBuilder()
                        .url(newUrl)
                        .build()

                    return chain.proceed(requestBuilder)
                }
            })

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(builder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
    }
}
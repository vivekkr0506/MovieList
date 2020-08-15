package com.example.saveomovie.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.datasource.MovieDataSource
import com.example.saveomovie.models.MovieDetails
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(
    private val apiServices: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, MovieDetails>() {
    val movieLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, MovieDetails> {
        val movieDataSource = MovieDataSource(
            apiServices,
            compositeDisposable
        )
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}
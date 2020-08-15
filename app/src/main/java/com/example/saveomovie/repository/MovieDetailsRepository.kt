package com.example.saveomovie.repository

import androidx.lifecycle.LiveData

import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.datasource.MovieDetailsDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieDBInterface) {
    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {
        movieDetailsDataSource =
            MovieDetailsDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailsDataSource.fetchMovieDetails(movieId)
        return movieDetailsDataSource.downloadMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsDataSource.networkState
    }
}
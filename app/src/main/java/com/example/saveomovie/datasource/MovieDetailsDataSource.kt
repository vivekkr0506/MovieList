package com.example.saveomovie.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.util.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsDataSource(
    private val apiServices: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {


    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadMovieDetailsResponse: LiveData<MovieDetails>
        get() = _downloadMovieDetailsResponse


    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiServices.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _downloadMovieDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDetails", it.message)
                    })
            )
        } catch (e: Exception) {

        }

    }
}
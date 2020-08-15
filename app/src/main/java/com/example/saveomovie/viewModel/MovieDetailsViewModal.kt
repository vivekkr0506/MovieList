package com.example.saveomovie.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.repository.MovieDetailsRepository
import com.example.saveomovie.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModal(
    private val movieDetailsRepository: MovieDetailsRepository,
    movieId: Int
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
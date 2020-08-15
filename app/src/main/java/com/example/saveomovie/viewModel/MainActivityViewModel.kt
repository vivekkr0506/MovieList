package com.example.saveomovie.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.repository.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val moviePagedList: LiveData<PagedList<MovieDetails>> by lazy {
        movieRepository.fetchMoviePagedList(compositeDisposable)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
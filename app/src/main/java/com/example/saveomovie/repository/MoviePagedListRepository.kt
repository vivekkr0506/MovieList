package com.example.saveomovie.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.datasource.MovieDataSource
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.util.POST_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiServices: MovieDBInterface) {
    lateinit var moviePagedList: LiveData<PagedList<MovieDetails>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<MovieDetails>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiServices, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState
        )

    }
}
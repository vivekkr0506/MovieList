package com.example.saveomovie.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.util.FIRST_PAGE
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDataSource(
    private val apiServices: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, MovieDetails>() {
    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieDetails>
    ) {
        networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiServices.getPopularMovie(page)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        callback.onResult(it.results, null, page + 1)
                        Log.e("Popular Movies", it.toString())
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("Popular Movies VIVEK", it.message)
                    })
            )
        } catch (e: Exception) {

        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieDetails>) {
        try {
            compositeDisposable.add(
                apiServices.getPopularMovie(params.key)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if (it.total_pages >= params.key) {
                            callback.onResult(it.results, params.key)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("Popular Movies", it.message)
                    })
            )
        } catch (e: Exception) {

        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieDetails>) {
        TODO("Not yet implemented")
    }
}
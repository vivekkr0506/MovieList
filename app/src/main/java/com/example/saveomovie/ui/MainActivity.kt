package com.example.saveomovie.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saveomovie.API.MovieDBClient
import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.R
import com.example.saveomovie.adapter.CrouselAdapter
import com.example.saveomovie.adapter.MoviePagedListAdapter
import com.example.saveomovie.repository.MoviePagedListRepository
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.global_toolbar.*


class MainActivity : AppCompatActivity() {
    private var mContext: Context = this
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var movieRepository: MoviePagedListRepository


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvToolbarTitle.text = "Movies"
        tvNewMovies.visibility = View.VISIBLE

        val apiServices: MovieDBInterface = MovieDBClient.getClient()
        movieRepository =
            MoviePagedListRepository(
                apiServices
            )
        viewModel = getViewModal()

        /*
        Adding MovieList Adapter
         */
        val movieListAdapter =
            MoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieListAdapter.getItemViewType(position)
                if (viewType == movieListAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }
        rvMoviesList.layoutManager = gridLayoutManager
        rvMoviesList.setHasFixedSize(true)
        rvMoviesList.adapter = movieListAdapter

        /*
        Adding CrouselAdapter
         */
        val crouselAdapter =
            CrouselAdapter(this)
        rvCrousel.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
            adapter = crouselAdapter
        }


        /*
        Added responseData to Adapter
         */
        viewModel.moviePagedList.observe(this, Observer {
            movieListAdapter.submitList(it)
            crouselAdapter.submitList(it)
        })
        /*
        Handling NetworkState
         */
        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (viewModel.listIsEmpty()) {
                movieListAdapter.setNetworkState(it)
            }
        })

       /*
       OnScrollChangeListener for Title
        */
//        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            Log.d(
//                " FragmentActivity.TAG",
//                "onScrollChangeForY - scrollY: $scrollY oldScrollY: $oldScrollY"
//            )
//            if (scrollY > 700) {
//                tvToolbarTitle.text = "Now Showing"
//            } else {
//                tvToolbarTitle.text = "Movies"
//            }
//        })

        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 700) {
                tvToolbarTitle.text = "Now Showing"
            } else {
                tvToolbarTitle.text = "Movies"
            }
        }
    }


    /*
    Getting ViewModel object for MainActivityViewModel
     */
    private fun getViewModal(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(
                    movieRepository
                ) as T
            }

        })[MainActivityViewModel::class.java]
    }
}
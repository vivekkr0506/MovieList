package com.example.saveomovie.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.saveomovie.R
import com.example.saveomovie.API.MovieDBClient
import com.example.saveomovie.API.MovieDBInterface
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.repository.MovieDetailsRepository
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.util.POSTER_BASE_URL
import com.example.saveomovie.viewModel.MovieDetailsViewModal
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.global_toolbar.*

class MovieDetailsActivity : AppCompatActivity() {
    private var mContext: Context = this
    private lateinit var viewModal: MovieDetailsViewModal
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        tvToolbarTitle.text = "Movies"
        ivSearchButton.setImageDrawable((ContextCompat.getDrawable(mContext, R.drawable.ic_upload)))
        ivDrawerButton.setImageDrawable((ContextCompat.getDrawable(mContext, R.drawable.ic_back)))
        ivDrawerButton.setOnClickListener {
            onBackPressed()
        }
        val movieId = intent.getIntExtra("id", 1)
        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieDetailsRepository =
            MovieDetailsRepository(apiService)

        viewModal = getViewModal(movieId)
        viewModal.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModal.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(movieDetails: MovieDetails) {
        movie_title.text = movieDetails.title
        tvReleaseDate.text = movieDetails.release_date
        if (movieDetails.adult) {
            tvRated.text = "A"
        } else {
            tvRated.text = "UA"
        }
        tvDetails.text = movieDetails.overview


        val moviePosterUrl = POSTER_BASE_URL + movieDetails.poster_path
        Glide.with(this).load(moviePosterUrl).into(iv_movie_poster)
    }


    private fun getViewModal(movieId: Int): MovieDetailsViewModal {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return modelClass.getConstructor(
                    MovieDetailsRepository::class.java,
                    Int::class.java
                )
                    .newInstance(movieDetailsRepository, movieId) as T
            }
        })[MovieDetailsViewModal::class.java]
    }
}
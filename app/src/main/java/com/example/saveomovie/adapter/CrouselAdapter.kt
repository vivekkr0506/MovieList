package com.example.saveomovie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.saveomovie.R
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.ui.MovieDetailsActivity
import com.example.saveomovie.util.POSTER_BASE_URL
import kotlinx.android.synthetic.main.crousel_list.view.*
import kotlinx.android.synthetic.main.movies_list.view.*

class CrouselAdapter(private val context: Context) :
    PagedListAdapter<MovieDetails, RecyclerView.ViewHolder>(MoviePagedListAdapter.MovieDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.crousel_list, parent, false)
            return CrouselHolder(view)
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CrouselHolder).bind(getItem(position+3), context)
    }

    class CrouselHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: MovieDetails?, context: Context) {
            val moviePosterUrl = POSTER_BASE_URL + movie?.poster_path
            Glide.with(itemView.context).load(moviePosterUrl).into(itemView.ivImageCrousel)

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }

    }
}
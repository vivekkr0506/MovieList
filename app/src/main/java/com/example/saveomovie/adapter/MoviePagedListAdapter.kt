package com.example.saveomovie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.saveomovie.R
import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.ui.MovieDetailsActivity
import com.example.saveomovie.util.NetworkState
import com.example.saveomovie.util.POSTER_BASE_URL
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.movies_list.view.*

class MoviePagedListAdapter(private val context: Context) :
    PagedListAdapter<MovieDetails, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    val networkState : NetworkState?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       // networkState = NetworkState
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movies_list, parent, false)
            return MovieListItemHolder(
                view
            )
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateViewHolder(
                view
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieListItemHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)

        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED

    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieDetails>() {
        override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
            return oldItem == newItem
        }
    }

    class MovieListItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: MovieDetails?, context: Context) {
            val moviePosterUrl = POSTER_BASE_URL + movie?.poster_path
            Glide.with(itemView.context).load(moviePosterUrl).into(itemView.ivMovie)

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar.visibility = View.VISIBLE
            } else {
                itemView.progress_bar.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.txt_error.visibility = View.VISIBLE
                itemView.txt_error.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.txt_error.visibility = View.VISIBLE
                itemView.txt_error.text = networkState.msg
            } else {
                itemView.txt_error.visibility = View.GONE
            }
        }
    }


    fun setNetworkState( newNetworkState : NetworkState){
        var previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        previousState =  newNetworkState
        var hasExtraRow = hasExtraRow();

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }
            else{
                notifyItemInserted(super.getItemCount())
            }
        }else if(hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount - 1)
        }
    }
}
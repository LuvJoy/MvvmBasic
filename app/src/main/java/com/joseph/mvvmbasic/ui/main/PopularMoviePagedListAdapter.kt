package com.joseph.mvvmbasic.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.joseph.mvvmbasic.data.api.IMAGE_URL
import com.joseph.mvvmbasic.data.api.NetworkState
import com.joseph.mvvmbasic.data.model.Movie
import com.joseph.mvvmbasic.databinding.MovieListItemBinding
import com.joseph.mvvmbasic.databinding.NetworkStateItemBinding
import com.joseph.mvvmbasic.ui.detail.MovieDetailActivity

class PopularMoviePagedListAdapter(
        private val context: Context
) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewBinding

        when (viewType) {
            MOVIE_VIEW_TYPE -> {
                binding = MovieListItemBinding.inflate(layoutInflater, parent, false)
                return MovieItemViewHolder(binding)
            }
            NETWORK_VIEW_TYPE -> {
                binding = NetworkStateItemBinding.inflate(layoutInflater, parent, false)
                return NetworkStateItemViewHolder(binding)
            }
            else -> {
                binding = NetworkStateItemBinding.inflate(layoutInflater, parent, false)
                return NetworkStateItemViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position)!!, context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState!!)
        }
    }

    // DiffCallback
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
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

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()

        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        }else if(hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount -1)
        }
    }

    //MovieItemViewHolder
    inner class MovieItemViewHolder(
            val binding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var item: Movie

        fun bind(movie: Movie, context: Context) {
            item = movie

            binding.root.setOnClickListener(this)
            binding.apply {

                cvMovieTitle.text = movie.title
                cvMovieReleaseDate.text = movie.releaseDate

                val moviePosterURL = IMAGE_URL + movie.posterPath
                Glide.with(itemView.context)
                        .load(moviePosterURL)
                        .into(binding.cvIvMoviePoster)
            }
        }

        override fun onClick(v: View?) {
            when (v) {
                binding.root -> {
                    val intent = Intent(itemView.context, MovieDetailActivity::class.java)
                    intent.putExtra("id", item.id)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    // NetworkStateItemViewholder
    inner class NetworkStateItemViewHolder(
            val binding: NetworkStateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState) {

            when (networkState) {
                NetworkState.LOADING -> {
                    binding.progressBarItem.visibility = View.VISIBLE
                }
                NetworkState.ERROR -> {
                    binding.errorMsgItem.visibility = View.VISIBLE
                    binding.errorMsgItem.text = networkState.msg
                }
                NetworkState.ENDOFLIST -> {
                    binding.errorMsgItem.visibility = View.VISIBLE
                    binding.errorMsgItem.text = networkState.msg
                }
                else -> {
                    binding.progressBarItem.visibility = View.GONE
                }
            }
        }
    }


}
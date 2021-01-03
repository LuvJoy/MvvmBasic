package com.joseph.mvvmbasic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.bumptech.glide.Glide
import com.joseph.mvvmbasic.R
import com.joseph.mvvmbasic.data.api.IMAGE_URL
import com.joseph.mvvmbasic.data.api.NetworkState
import com.joseph.mvvmbasic.data.api.TheMovieDbClient
import com.joseph.mvvmbasic.data.model.MovieDetails
import com.joseph.mvvmbasic.data.repository.MovieDetailsRepository
import com.joseph.mvvmbasic.data.repository.SingleMovieViewModel
import com.joseph.mvvmbasic.data.repository.SingleMovieViewModelFactory
import com.joseph.mvvmbasic.databinding.ActivityMovieDetailBinding
import java.text.NumberFormat
import java.util.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("id", 0)
        val apiService = TheMovieDbClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId, movieRepository)
        viewModel.movieDetails.observe(this@MovieDetailActivity, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this@MovieDetailActivity, Observer { networkState ->
            binding.apply {
                progressBar.visibility =
                    if (networkState == NetworkState.LOADING) View.VISIBLE else View.GONE
                errorTextview.visibility =
                    if (networkState == NetworkState.ERROR) View.VISIBLE else View.GONE
            }
        })

    }

    fun bindUI(item: MovieDetails) {
        binding.apply {
            movieTitle.text = item.title
            movieTagline.text = item.tagline
            movieReleaseDate.text = item.releaseDate
            movieRating.text = item.voteAverage.toString()
            movieRuntime.text = item.runtime.toString() + " min"
            movieOverview.text = item.overview

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            movieBudget.text = formatCurrency.format(item.budget)
            movieRevenue.text = formatCurrency.format(item.revenue)

            val moviePosterURL = IMAGE_URL + item.posterPath
            Glide.with(this@MovieDetailActivity)
                .load(moviePosterURL)
                .into(ivMoviePoster)
        }
    }

    private fun getViewModel(
        movieId: Int,
        repository: MovieDetailsRepository
    ): SingleMovieViewModel {
        return ViewModelProvider(this@MovieDetailActivity,
            SingleMovieViewModelFactory(repository, movieId))
            .get(SingleMovieViewModel::class.java)
    }
}
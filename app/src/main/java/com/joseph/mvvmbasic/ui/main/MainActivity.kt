package com.joseph.mvvmbasic.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.joseph.mvvmbasic.data.api.NetworkState
import com.joseph.mvvmbasic.data.api.TheMovieDbClient
import com.joseph.mvvmbasic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var movieAdapter: PopularMoviePagedListAdapter

    lateinit var movieRepository: MoviePagedListRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = TheMovieDbClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()
        setRecyclerView()

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.apply {
                progressBarPopular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                errorTextviewPopular.visibility = if(viewModel.listIsEmpty() && it==NetworkState.ERROR) View.VISIBLE else View.GONE
            }
            if(!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this@MainActivity, MainActivityViewModelFactory(movieRepository))
                .get(MainActivityViewModel::class.java)
    }

    private fun setRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        movieAdapter = PopularMoviePagedListAdapter(this)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }

        binding.rvMovieList.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }
}
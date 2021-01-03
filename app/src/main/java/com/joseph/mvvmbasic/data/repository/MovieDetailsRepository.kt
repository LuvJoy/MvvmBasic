package com.joseph.mvvmbasic.data.repository

import android.app.Service
import androidx.lifecycle.LiveData
import com.joseph.mvvmbasic.data.api.NetworkState
import com.joseph.mvvmbasic.data.api.TheMovieDbInterface
import com.joseph.mvvmbasic.data.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(
    private val apiService: TheMovieDbInterface
) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }

}
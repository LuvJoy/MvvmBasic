package com.joseph.mvvmbasic.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.joseph.mvvmbasic.data.api.NetworkState
import com.joseph.mvvmbasic.data.api.POST_PER_PAGE
import com.joseph.mvvmbasic.data.api.TheMovieDbInterface
import com.joseph.mvvmbasic.data.model.Movie
import com.joseph.mvvmbasic.data.repository.MovieDataSource
import com.joseph.mvvmbasic.data.repository.MovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(
        private val apiService: TheMovieDbInterface
) {
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(POST_PER_PAGE)
                .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetwokrState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
                movieDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState)
    }
}
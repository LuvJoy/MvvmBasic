package com.joseph.mvvmbasic.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.joseph.mvvmbasic.data.api.NetworkState
import com.joseph.mvvmbasic.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(
        private val movieRepository: MoviePagedListRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }
    val networkString: LiveData<NetworkState> by lazy {
        movieRepository.getNetwokrState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
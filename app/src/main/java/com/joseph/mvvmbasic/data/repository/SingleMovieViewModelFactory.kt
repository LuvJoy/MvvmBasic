package com.joseph.mvvmbasic.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SingleMovieViewModelFactory(
    private val repository: MovieDetailsRepository,
    val movieId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SingleMovieViewModel::class.java)) {
            SingleMovieViewModel(repository, movieId) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
package com.joseph.mvvmbasic.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joseph.mvvmbasic.data.repository.SingleMovieViewModel
import java.lang.IllegalArgumentException

class MainActivityViewModelFactory(
        private val repository: MoviePagedListRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            MainActivityViewModel(repository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
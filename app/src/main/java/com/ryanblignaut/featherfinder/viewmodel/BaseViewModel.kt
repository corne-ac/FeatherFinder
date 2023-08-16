package com.ryanblignaut.featherfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel<T> : ViewModel() {
    private val _mut = MutableLiveData<Result<T>>()
    val live: LiveData<Result<T>> = _mut
    protected fun fetchInBackground(ioBlock: suspend () -> Result<T>) {
        viewModelScope.launch(Dispatchers.IO) {
            _mut.postValue(ioBlock())
        }
    }
}
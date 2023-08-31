package com.ryanblignaut.featherfinder.viewmodel.helper

import androidx.lifecycle.MutableLiveData

open class FormViewModel<T> : BaseViewModel<T>(), IFormState {
    private var _formState = MutableLiveData<MutableMap<String, String?>>()
    val formState: MutableLiveData<MutableMap<String, String?>> = _formState

    init {
        _formState.value = mutableMapOf()
    }

    override fun dataChanged(key: String, value: String) {
        _formState.value?.set(key, value)
        _formState.postValue(_formState.value)
    }

    override fun getData(key: String): String? = _formState.value?.get(key)


}
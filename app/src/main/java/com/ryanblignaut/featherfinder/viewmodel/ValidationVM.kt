package com.ryanblignaut.featherfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class ValidationVM<T> : BaseViewModel<T>() {
    private val _validationState = MutableLiveData<ValidationState<T>>()
    val validationState: LiveData<ValidationState<T>>
        get() = _validationState

    fun validate(validationItems: List<ValidationItem<T>>) {
        _validationState.value = ValidationState(validationItems)
    }
}

data class ValidationItem<T>(
    val data: T,
    val validator: (T) -> Boolean,
    val errorMessage: String,
)

data class ValidationState<T>(
    val validationItems: List<ValidationItem<T>>,
)
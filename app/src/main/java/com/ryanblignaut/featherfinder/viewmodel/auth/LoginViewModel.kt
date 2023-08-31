package com.ryanblignaut.featherfinder.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.fire.FireBase
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel
import com.ryanblignaut.featherfinder.viewmodel.helper.IFormState

class LoginViewModel : BaseViewModel<FirebaseUser>(), IFormState {
    private var _formState = MutableLiveData<MutableMap<String, String?>>()
    val formState: MutableLiveData<MutableMap<String, String?>> = _formState

    init {
        _formState.value = mutableMapOf()
    }

    fun login(email: String, password: String) = fetchInBackground {
        FireBase.signInWithEmailAndPassword(email, password)
    }

    override fun dataChanged(key: String, value: String) {
        _formState.value?.set(key, value)
        _formState.postValue(_formState.value)
    }

    override fun getData(key: String): String? = _formState.value?.get(key)

}


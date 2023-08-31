package com.ryanblignaut.featherfinder.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.fire.FireBase

class LoginView2Model : BaseViewModel<FirebaseUser>(), Mappable {

    /*   data class test(val m: MutableMap<String, String?> = mutableMapOf())

       private var map: MutableMap<String, String?> = mutableMapOf()*/
    private var _loginFormState = MutableLiveData<MutableMap<String, String?>>()
    val loginFormState: MutableLiveData<MutableMap<String, String?>> = _loginFormState

    init {
        _loginFormState.value = mutableMapOf()
    }

    fun login(email: String, password: String) = fetchInBackground {
        FireBase.signInWithEmailAndPassword(email, password)
    }

    override fun dataChanged(key: String, value: String) {
        _loginFormState.value?.set(key, value)
        _loginFormState.postValue(_loginFormState.value)
    }

    override fun getData(key: String): String? = _loginFormState.value?.get(key)

}


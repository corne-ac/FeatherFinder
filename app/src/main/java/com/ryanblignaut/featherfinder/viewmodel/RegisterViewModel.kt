package com.ryanblignaut.featherfinder.viewmodel

import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.fire.FireBase

class RegisterViewModel : BaseViewModel<FirebaseUser>() {
    fun fetchRoute(email: String, password: String) = fetchInBackground {
        FireBase.registerUser(email, password)
    }



}


/*
class RouteViewModel : ViewModel() {
    private val _mut = MutableLiveData<Result<JSONObject>>()
    val live: LiveData<Result<JSONObject>> = _mut

    fun fetchRoute(startLon: Double, startLat: Double, endLon: Double, endLat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                OpenRoutesApi().fetchRoute(startLat, startLon, endLon, endLat, -1).map(::JSONObject)
            _mut.postValue(result)
        }
    }
}*/

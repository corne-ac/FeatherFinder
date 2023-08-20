package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.OpenRoutesApi
import org.json.JSONObject

class RouteViewModel : BaseViewModel<JSONObject>() {
    fun fetchRoute(startLon: Double, startLat: Double, endLon: Double, endLat: Double) =
        fetchInBackground { OpenRoutesApi().fetchRoute(startLat, startLon, endLon, endLat, -1).map(::JSONObject) }
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

package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.model.EBirdLocation

class BirdingHotspotViewModel : BaseViewModel<Array<EBirdLocation>>() {
    fun fetchHotspots(latitude: Double, longitude: Double) =
        fetchInBackground { EBirdApi.fetchNearbyHotspots(latitude, longitude, 20) }
}

/*class BirdingHotspotViewModel : BaseViewModel<Result<Array<EBirdLocation>>>() {
    private val _hotspots = MutableLiveData<Result<Array<EBirdLocation>>>()
    val hotspots: LiveData<Result<Array<EBirdLocation>>> = _hotspots

    fun fetchHotspots(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = EBirdApi.fetchNearbyHotspots(latitude, longitude, 20)
            _hotspots.postValue(response)
        }
    }
}*/
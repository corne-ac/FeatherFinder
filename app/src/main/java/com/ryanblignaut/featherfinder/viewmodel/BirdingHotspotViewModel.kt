package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.model.api.EBirdLocation
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class BirdingHotspotViewModel : BaseViewModel<Array<EBirdLocation>>() {
    fun fetchHotspots(latitude: Double, longitude: Double, dist: Float) =
        fetchInBackground { EBirdApi.fetchNearbyHotspots(latitude, longitude, dist) }
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
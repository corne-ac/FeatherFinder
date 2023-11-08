package com.ryanblignaut.featherfinder.viewmodel.observation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.api.EBirdSpecies
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddObservationViewModel : FormViewModel<String>() {

    private val _mutBirdList = MutableLiveData<Result<Array<EBirdSpecies>>>()
    val liveBirdSpecies: LiveData<Result<Array<EBirdSpecies>>> = _mutBirdList

    fun saveObservation(observation: BirdObservation) = fetchInBackground {
        FirestoreDataManager.saveObservation(observation)
    }

    fun fetchLiveBirds(lat: Double, lng: Double) = run {
        viewModelScope.launch(Dispatchers.IO) {
            _mutBirdList.postValue(EBirdApi.fetchNearObservations(lat, lng))
        }
    }

}

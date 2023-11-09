package com.ryanblignaut.featherfinder.viewmodel.observation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.BirdObsDetails
import com.ryanblignaut.featherfinder.model.BirdObsTitle
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.FullBirdObservation
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllObservationsViewModel : BaseViewModel<List<BirdObsTitle>?>() {

    fun getObservations() = fetchInBackground {
        FirestoreDataManager.requestObservationIdList()
    }

//    fun getObservationsLocations() = fetchInBackground {
//        FirestoreDataManager.requestAllObservations()
//    }

    private val _mutDetails = MutableLiveData<Result<List<FullBirdObservation>?>>()
    val liveDetails: LiveData<Result<List<FullBirdObservation>?>> = _mutDetails
    fun getObservationsLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            _mutDetails.postValue(FirestoreDataManager.requestAllObservations())
        }
    }

    fun getObservationsSorted(filterTime: String, nameSort: Boolean) = fetchInBackground {
        FirestoreDataManager.requestObservationIdListFiltered(filterTime, nameSort)
    }
}

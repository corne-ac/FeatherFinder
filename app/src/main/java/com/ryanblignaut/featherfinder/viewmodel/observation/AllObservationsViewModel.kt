package com.ryanblignaut.featherfinder.viewmodel.observation

import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class AllObservationsViewModel : BaseViewModel<List<BirdObservation>>() {
    fun getObservations() = fetchInBackground {
        Result.success(FirebaseDataManager.getAllObservations())
    }
}

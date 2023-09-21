package com.ryanblignaut.featherfinder.viewmodel.observation

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class AddObservationViewModel : FormViewModel<String>() {
    fun saveObservation(observation: BirdObservation) = fetchInBackground {
        FirestoreDataManager.saveObservation(observation)
    }
}

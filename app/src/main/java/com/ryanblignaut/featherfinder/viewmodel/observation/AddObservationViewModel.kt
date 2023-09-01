package com.ryanblignaut.featherfinder.viewmodel.observation

import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class AddObservationViewModel : FormViewModel<BirdObservation>() {
    fun saveObservation(observation: BirdObservation) = fetchInBackground {
        FirebaseDataManager.saveObservation(observation)
    }
}

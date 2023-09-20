package com.ryanblignaut.featherfinder.viewmodel.observation

import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class DetailObservationViewModel : FormViewModel<BirdObservation?>() {
    fun getObservationById(birdId: String) = fetchInBackground {
        FirebaseDataManager.getObservationById(birdId)
    }
}

package com.ryanblignaut.featherfinder.viewmodel.observation

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.BirdObsDetails
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class DetailObservationViewModel : FormViewModel<BirdObsDetails?>() {
    fun getObservationById(birdId: String) = fetchInBackground {
        FirestoreDataManager.requestObservationById(birdId)
    }
}

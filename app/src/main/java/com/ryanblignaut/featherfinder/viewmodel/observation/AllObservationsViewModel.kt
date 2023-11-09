package com.ryanblignaut.featherfinder.viewmodel.observation

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.BirdObsTitle
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class AllObservationsViewModel : BaseViewModel<List<BirdObsTitle>?>() {

    fun getObservations() = fetchInBackground {
        FirestoreDataManager.requestObservationIdList()
    }

    fun getObservationsSorted(filterTime: String, nameSort: Boolean) = fetchInBackground {
        FirestoreDataManager.requestObservationIdListFiltered(filterTime, nameSort)
    }
}

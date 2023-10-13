package com.ryanblignaut.featherfinder.viewmodel.home

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.UserSettings
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class HomeViewModel : BaseViewModel<UserSettings?>() {
    fun getUserSettings() = fetchInBackground {
        FirestoreDataManager.getSettings()
    }

}
package com.ryanblignaut.featherfinder.viewmodel.goal

import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class AllGoalsViewModel : BaseViewModel<List<Goal>>() {
    fun getGoals() = fetchInBackground {
        Result.success(FirebaseDataManager.getAllGoals())
    }
}

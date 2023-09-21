package com.ryanblignaut.featherfinder.viewmodel.goal

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.GoalTitle
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class AllGoalsViewModel : BaseViewModel<List<GoalTitle>?>() {
    fun getGoals() = fetchInBackground {
        FirestoreDataManager.requestGoalTitleList()
    }
}

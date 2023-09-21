package com.ryanblignaut.featherfinder.viewmodel.goal

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class GoalViewModel : FormViewModel<String>() {
    fun saveGoal(goal: Goal) = fetchInBackground {
        FirestoreDataManager.saveGoal(goal)
    }
}

package com.ryanblignaut.featherfinder.viewmodel.goal

import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class GoalViewModel : FormViewModel<Goal>() {
    fun saveGoal(goal: Goal) = fetchInBackground {
        FirebaseDataManager.saveGoal(goal)
    }

    fun getGoals(id: String) = fetchInBackground {
        FirebaseDataManager.getGoal(id)
    }
}

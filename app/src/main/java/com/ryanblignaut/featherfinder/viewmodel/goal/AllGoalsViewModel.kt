package com.ryanblignaut.featherfinder.viewmodel.goal

import android.media.MediaPlayer
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.FullGoal
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class AllGoalsViewModel : BaseViewModel<List<FullGoal>?>() {
    fun getGoals() = fetchInBackground {
        FirestoreDataManager.requestFullGoalList()
    }

    fun deleteGoal(goal: String) = runAsync {
        val deleteGoal = FirestoreDataManager.deleteGoal(goal)

        if (deleteGoal.isFailure) {
            println("Failed to delete goal")
            println(deleteGoal.exceptionOrNull())
            return@runAsync
        }
        val media = MediaPlayer()
        media.setDataSource("https://www.myinstants.com/media/sounds/untitled_1071.mp3")
        media.prepare()
        media.setOnCompletionListener {
            media.release()
        }
        media.start()
    }

    fun completeGoal(id: String) = runAsync {
        FirestoreDataManager.completeGoal(id)
    }

    fun removeCompletionOnGoal(id: String) = runAsync {
        FirestoreDataManager.removeCompletionOnGoal(id)
    }


}

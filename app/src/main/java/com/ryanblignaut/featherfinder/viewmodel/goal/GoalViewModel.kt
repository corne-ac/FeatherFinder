package com.ryanblignaut.featherfinder.viewmodel.goal

import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.fire.FirebaseManager
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class GoalViewModel : FormViewModel<FirebaseUser>() {
    fun saveGoal(email: String, password: String) = fetchInBackground {

        FirebaseManager.signInWithEmailAndPassword(email, password)
    }



}


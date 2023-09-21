package com.ryanblignaut.featherfinder.viewmodel.auth

import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class RegisterViewModel : FormViewModel<FirebaseUser>() {
    fun register(username: String, email: String, password: String) = fetchInBackground {
        val user = FirebaseAuthManager.registerUser(email, password)
        if (user.isSuccess) {
            val addUser = FirestoreDataManager.addUser(username)
            /*FirebaseDataManager.addUser(user.getOrNull()!!.uid, username)*/
            if (addUser.isFailure) {
                FirebaseAuthManager.getCurrentUser()?.delete()
                return@fetchInBackground Result.failure(addUser.exceptionOrNull()!!)
            }
        }
        return@fetchInBackground user
    }
}


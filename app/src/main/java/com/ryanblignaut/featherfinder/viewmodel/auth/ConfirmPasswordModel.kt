package com.ryanblignaut.featherfinder.viewmodel.auth

import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel
import kotlinx.coroutines.tasks.await

class ConfirmPasswordModel : FormViewModel<Boolean>() {
    fun sendEmailVerify() = fetchInBackground {
        try {
            FirebaseAuthManager.getCurrentUser()?.sendEmailVerification()?.await()
        } catch (e: Exception) {
            return@fetchInBackground Result.failure(e)
        }
        return@fetchInBackground Result.success(true)
    }
}

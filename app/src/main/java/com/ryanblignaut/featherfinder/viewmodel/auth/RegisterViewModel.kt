package com.ryanblignaut.featherfinder.viewmodel.auth

import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.fire.FirebaseManager
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class RegisterViewModel : FormViewModel<FirebaseUser>() {
    fun register(email: String, password: String) = fetchInBackground {
        FirebaseManager.registerUser(email, password)
    }
}


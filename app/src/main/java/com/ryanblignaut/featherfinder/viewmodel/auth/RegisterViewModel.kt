package com.ryanblignaut.featherfinder.viewmodel.auth

import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class RegisterViewModel : FormViewModel<FirebaseUser>() {
    fun register(email: String, password: String) = fetchInBackground {
        FirebaseAuthManager.registerUser(email, password)
    }
}


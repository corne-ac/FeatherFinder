package com.ryanblignaut.featherfinder.viewmodel.auth

import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.viewmodel.helper.FormViewModel

class LoginViewModel : FormViewModel<FirebaseUser>() {
    fun login(email: String, password: String) = fetchInBackground {
        FirebaseAuthManager.signInWithEmailAndPassword(email, password)
    }
}


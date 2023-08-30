package com.ryanblignaut.featherfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.fire.FireBase
import com.ryanblignaut.featherfinder.ui.auth.LoginFormState
import com.ryanblignaut.featherfinder.utils.DataValidator

class LoginViewModel : BaseViewModel<FirebaseUser>() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    fun login(email: String, password: String) = fetchInBackground {
        FireBase.signInWithEmailAndPassword(email, password)
    }

    fun dataChanged(email: String?, password: String?) {
        val emailError = when {
            email == null -> null
            DataValidator.isEmpty(email) -> R.string.invalid_empty_email
            DataValidator.invalidEmail(email) -> R.string.invalid_email
            else -> null
        }
    /*    if (emailError != null) {
            _loginForm.value = LoginFormState(emailError = emailError)
            return
        }*/

        val passwordError = when {
            password == null -> null
            DataValidator.isEmpty(password) -> R.string.invalid_empty_password
            DataValidator.invalidPassword(password) -> R.string.invalid_password
            else -> null
        }
        if (passwordError != null || emailError != null) {
            _loginForm.value = LoginFormState(passwordError = passwordError, emailError = emailError)
            return
        }
        _loginForm.value = LoginFormState(isDataValid = true)
    }
}

package com.ryanblignaut.featherfinder.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.fire.FireBase

class RegisterViewModel : BaseViewModel<FirebaseUser>() {
    // LiveData for observing validation errors
    private val _validationErrors = MutableLiveData<ValidationErrors>()
    val validationErrors: LiveData<ValidationErrors> = _validationErrors


    // Data fields for registration
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var confirmPassword: String = ""

    // Function to perform validation
    fun validateEmail() {
        val errors = _validationErrors.value ?: ValidationErrors()
        if (email.isBlank()) {
            errors.emailError = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.emailError = "Invalid email address"
        } else {
            errors.emailError = null
        }
        _validationErrors.value = errors
    }

    fun validatePassword() {
        val errors = _validationErrors.value ?: ValidationErrors()
        errors.passwordError = if (password.isBlank()) {
            "Password is required"
        } else if (password.length !in 6..20) {
            "Invalid password length must be between 6 and 20 characters"
        } else {
            null
        }
        _validationErrors.value = errors
    }


    fun registerUserInFirebase(email: String, password: String) = fetchInBackground {
        FireBase.registerUser(email, password)
    }
}

data class ValidationErrors(
    var usernameError: String? = null,
    var emailError: String? = null,
    var passwordError: String? = null
)
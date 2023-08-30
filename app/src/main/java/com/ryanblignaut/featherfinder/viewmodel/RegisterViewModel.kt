package com.ryanblignaut.featherfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.fire.FireBase
import com.ryanblignaut.featherfinder.ui.auth.RegisterScreenState
import com.ryanblignaut.featherfinder.utils.DataValidator


class RegisterViewModel : BaseViewModel<FirebaseUser>() {
    private val _registerForm = MutableLiveData<RegisterScreenState>()
    val registerFormState: LiveData<RegisterScreenState> = _registerForm

    fun registerUserInFirebase(email: String, password: String) = fetchInBackground {
        FireBase.registerUser(email, password)
    }


    fun dataChanged(
        username: String?,
        email: String?,
        password: String?,
        confirmPassword: String?,
    ) {
        val usernameError = when {
            username == null -> null
            DataValidator.isEmpty(username) -> R.string.invalid_empty_username
            DataValidator.invalidUsername(username) -> R.string.invalid_username
            else -> null
        }
        val emailError = when {
            email == null -> null
            DataValidator.isEmpty(email) -> R.string.invalid_empty_email
            DataValidator.invalidEmail(email) -> R.string.invalid_email
            else -> null
        }
        val passwordError = when {
            password == null -> null
            DataValidator.isEmpty(password) -> R.string.invalid_empty_password
            DataValidator.invalidPassword(password) -> R.string.invalid_password
            else -> null
        }
        val confirmPasswordError = when {
            confirmPassword == null -> null
            password == null -> null
            DataValidator.isEmpty(confirmPassword) -> R.string.invalid_empty_confirm_password
            password != confirmPassword -> R.string.invalid_confirm_password
            else -> null
        }
        if (confirmPasswordError != null || passwordError != null || emailError != null || usernameError != null) {
            _registerForm.value = RegisterScreenState(
                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
            return
        }
        _registerForm.value = RegisterScreenState(isDataValid = true)
    }

    /*  fun dataChanged(
          username: String?,
          email: String?,
          password: String?,
          confirmPassword: String?,
      ) {
          val usernameError = when {
              username == null -> null
              DataValidator.isEmpty(username) -> R.string.invalid_empty_username
              DataValidator.invalidUsername(username) -> R.string.invalid_username
              else -> null
          }
          if (usernameError != null) {
              _registerForm.value = RegisterScreenState(usernameError = usernameError)
              return
          }
          val emailError = when {
              email == null -> null
              DataValidator.isEmpty(email) -> R.string.invalid_empty_email
              DataValidator.invalidEmail(email) -> R.string.invalid_email
              else -> null
          }
          if (emailError != null) {
              _registerForm.value = RegisterScreenState(emailError = emailError)
              return
          }
          val passwordError = when {
              password == null -> null
              DataValidator.isEmpty(password) -> R.string.invalid_empty_password
              DataValidator.invalidPassword(password) -> R.string.invalid_password
              else -> null
          }
          if (passwordError != null) {
              _registerForm.value = RegisterScreenState(passwordError = passwordError)
              return
          }

          val confirmPasswordError = when {
              confirmPassword == null -> null
              password == null -> null
              DataValidator.isEmpty(confirmPassword) -> R.string.invalid_empty_confirm_password
              password != confirmPassword -> R.string.invalid_confirm_password
              else -> null
          }
          if (confirmPasswordError != null) {
              _registerForm.value = RegisterScreenState(confirmPasswordError = confirmPasswordError)
              return
          }
          _registerForm.value = RegisterScreenState(isDataValid = true)
      }*/

}
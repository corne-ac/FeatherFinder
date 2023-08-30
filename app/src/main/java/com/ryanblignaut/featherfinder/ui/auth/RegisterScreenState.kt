package com.ryanblignaut.featherfinder.ui.auth

data class RegisterScreenState(
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false,
)
package com.ryanblignaut.featherfinder.ui.auth

data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false,
)
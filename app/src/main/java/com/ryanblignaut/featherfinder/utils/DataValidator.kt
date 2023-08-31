package com.ryanblignaut.featherfinder.utils

import android.util.Patterns
import com.ryanblignaut.featherfinder.R

object DataValidator {

    private fun isEmpty(input: String): Boolean {
        return input.isEmpty()
    }

    private fun invalidEmail(input: String): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    private fun invalidPassword(input: String): Boolean {
        return input.length <= 6
    }

    private fun invalidUsername(username: String): Boolean {
        return username.length <= 8
    }

    private fun invalidConfirmPassword(pass1: String, pass2: String): Boolean {
        return pass1 != pass2
    }

    fun usernameValidation(it: String?): Int? {
        return when {
            it == null -> null
            isEmpty(it) -> R.string.invalid_empty_username
            invalidUsername(it) -> R.string.invalid_username
            else -> null
        }
    }

    fun emailValidation(it: String?): Int? {
        return when {
            it == null -> null
            isEmpty(it) -> R.string.invalid_empty_email
            invalidEmail(it) -> R.string.invalid_email
            else -> null
        }
    }

    fun passwordValidation(it: String?): Int? {
        return when {
            it == null -> null
            isEmpty(it) -> R.string.invalid_empty_password
            invalidPassword(it) -> R.string.invalid_password
            else -> null
        }
    }

    fun confirmPasswordValidation(it: String?, other: String?): Int? {
        return when {
            it == null -> null
            other == null -> null
            isEmpty(it) -> R.string.invalid_empty_confirm_password
            invalidConfirmPassword(it, other) -> R.string.invalid_confirm_password
            else -> null
        }
    }
}
package com.ryanblignaut.featherfinder.utils

import android.util.Patterns
import com.ryanblignaut.featherfinder.R

object DataValidator {

    fun isEmpty(input: String): Boolean {
        return input.isEmpty()
    }

    fun invalidEmail(input: String): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    fun invalidPassword(input: String): Boolean {
        return input.length <= 6
    }

    fun invalidUsername(username: String): Boolean {
        return username.length <= 8
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

}
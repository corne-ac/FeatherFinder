package com.ryanblignaut.featherfinder.utils

import android.util.Patterns

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

}
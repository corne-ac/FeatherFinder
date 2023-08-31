package com.ryanblignaut.featherfinder.viewmodel.helper

interface IFormState {
    fun dataChanged(key: String, value: String)
    fun getData(key: String): String?
}
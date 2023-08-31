package com.ryanblignaut.featherfinder.viewmodel

interface Mappable {
    fun dataChanged(key: String, value: String)
    fun getData(key: String): String?
}
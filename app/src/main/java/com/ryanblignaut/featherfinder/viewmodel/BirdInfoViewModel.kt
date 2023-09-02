package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.XenoApi
import com.ryanblignaut.featherfinder.model.api.XenoResponse
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class BirdInfoViewModel : BaseViewModel<XenoResponse>() {
    fun fetchBirdsInLocation(loc: String) = fetchInBackground { XenoApi.fetchBirdsInLocation(loc) }
}
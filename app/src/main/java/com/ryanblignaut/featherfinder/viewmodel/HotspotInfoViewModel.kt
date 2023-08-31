package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.model.api.EBirdHotspotData
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class HotspotInfoViewModel : BaseViewModel<EBirdHotspotData>() {
    fun fetchHotspotInfo(hotspotId: String) =
        fetchInBackground { EBirdApi.fetchHotspotInfo(hotspotId) }
}
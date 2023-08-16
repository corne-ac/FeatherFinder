package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.model.EBirdHotspotData

class HotspotInfoViewModel : BaseViewModel<EBirdHotspotData>() {
    fun fetchHotspotInfo(hotspotId: String) =
        fetchInBackground { EBirdApi.fetchHotspotInfo(hotspotId) }
}
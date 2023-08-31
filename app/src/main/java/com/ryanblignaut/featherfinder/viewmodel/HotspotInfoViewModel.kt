package com.ryanblignaut.featherfinder.viewmodel

import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.model.EBirdHotspotData
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class HotspotInfoViewModel : BaseViewModel<EBirdHotspotData>() {
    fun fetchHotspotInfo(hotspotId: String) =
        fetchInBackground { EBirdApi.fetchHotspotInfo(hotspotId) }
}
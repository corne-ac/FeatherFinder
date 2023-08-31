package com.ryanblignaut.featherfinder.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.HotspotInfoViewModel


class HotspotInfo(private val hotspotID: String) : PreBindingFragment<FragmentMapBinding>() {
    private lateinit var hotspotInfoViewModel: HotspotInfoViewModel
    override fun addContentToView(savedInstanceState: Bundle?) {
        hotspotInfoViewModel = ViewModelProvider(this)[HotspotInfoViewModel::class.java]

        hotspotInfoViewModel.live.observe(viewLifecycleOwner) {


        }
        hotspotInfoViewModel.fetchHotspotInfo(hotspotID)


    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentMapBinding {
        return inflateBinding(inflater, container)
    }

}
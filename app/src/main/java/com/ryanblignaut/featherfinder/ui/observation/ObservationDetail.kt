package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentObservationAddBinding
import com.ryanblignaut.featherfinder.model.BirdObsDetails
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.observation.DetailObservationViewModel

/**
 * This class represents the user interface for viewing a bird observations.
 * It provides functionality by providing more details of bird sighting made by a user.
 * The list items will be able to be clicked in order to show more details of the sighting.
 */
class ObservationDetail : PreBindingFragment<FragmentObservationAddBinding>() {
    private val formViewModel: DetailObservationViewModel by viewModels()
    private val args: ObservationDetailArgs by navArgs()
    private lateinit var googleMap: SupportMapFragment
    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        formViewModel.getObservationById(args.observationId)
        binding.birdSpecies.setText(args.birdSpecies)
        binding.date.setText(args.date)
        googleMap = binding.miniMap.getFragment<SupportMapFragment>()
        googleMap.onCreate(savedInstanceState)
        formViewModel.live.observe(viewLifecycleOwner, ::loadObservationDetails)
    }

    private fun loadObservationDetails(result: Result<BirdObsDetails?>) {

        if (result.isFailure) {
            val throwable = result.exceptionOrNull()!!
            throwable.printStackTrace()
        }
        val values = result.getOrNull()!!
        binding.time.setText(values.time)
        //binding.pos.setText(values.lat)
        binding.notes.setText(values.notes)
        binding.saveObservationAction.isVisible = false

        //check if null location, display toggle
        if (values.lat != "" || values.long != "") {
            googleMap.getMapAsync { map ->
                val location = LatLng(values.lat.toDouble(), values.long.toDouble())
                val marker = MarkerOptions().position(location)
                map.addMarker(marker)
                map.moveCamera(CameraUpdateFactory.zoomTo(15F))
                map.moveCamera(CameraUpdateFactory.newLatLng(location))

                map.uiSettings.apply {
                    isScrollGesturesEnabled = false
                    isZoomGesturesEnabled = false
                    isTiltGesturesEnabled = false
                }
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
            binding.myLocSwitch.isVisible = false
            binding.myLocSwitch.isActivated = true
        } else {
            binding.myLocSwitch.isEnabled = false
            binding.myLocSwitch.isActivated = false
            binding.miniMap.isVisible = false
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): FragmentObservationAddBinding {
        return inflateBinding(inflater, container)
    }

}
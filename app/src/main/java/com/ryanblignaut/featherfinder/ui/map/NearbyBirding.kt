package com.ryanblignaut.featherfinder.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.model.api.EBirdLocation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.BirdingHotspotViewModel


private const val REQUEST_LOCATION_PERMISSIONS_CODE = 123
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

class NearbyBirding : PreBindingFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private lateinit var birdingHotspotViewModel: BirdingHotspotViewModel

    override fun addContentToView(savedInstanceState: Bundle?) {

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        val googleMap =
//            childFragmentManager.findFragmentById(com.ryanblignaut.featherfinder.R.id.map) as SupportMapFragment?

        //check for permissions

        if (hasLocationPermissions()) {
            val googleMap = binding.map.getFragment<SupportMapFragment>()
            googleMap.onCreate(savedInstanceState)
            googleMap.getMapAsync(this)
        } else {
            requestLocationPermissions()
        }
    }

    //Doesnt call this override method, dont know why. Does call it when in mainActivity, but cant handle it from there like this. hell knows.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray ) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (hasLocationPermissions()) {
                    val googleMap = binding.map.getFragment<SupportMapFragment>()
                    googleMap.onCreate(null) //null hier want donno, testing needed
                    googleMap.getMapAsync(this)
                }
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Location permission is required to show map.", Toast.LENGTH_SHORT).show()
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentMapBinding {
        return inflateBinding(inflater, container)
    }

    /*
        private val registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                var permissionGranted = true
                permissions.entries.forEach {
                    if (it.key in PERMISSIONS_REQUIRED && !it.value) permissionGranted = false
                }

                if (!permissionGranted) {
                    Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
                } else {
                    map.isMyLocationEnabled = true
                }
            }
    */

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        // Check if the user has given permission to use their location.

//        registerForActivityResult.launch(PERMISSIONS_REQUIRED)


        birdingHotspotViewModel = ViewModelProvider(this)[BirdingHotspotViewModel::class.java]
        val convertBirdLocationOntoMap = function(map)
        birdingHotspotViewModel.live.observe(viewLifecycleOwner, convertBirdLocationOntoMap)
        birdingHotspotViewModel.fetchHotspots(-25.873390, 28.197800)

    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun function(map: GoogleMap): (Result<Array<EBirdLocation>>) -> Unit {
        return {


            var p = LatLng(40.730610, -73.935242);
            map.clear()
//                .fillColor(Color.parseColor("#220000FF"))


            if (it.isSuccess) {
                it.getOrNull()!!.forEach { hotspot ->
                    // Skip the location if its latitude or longitude is null.
                    if (hotspot.lat == null || hotspot.lng == null) return@forEach
                    p = LatLng(hotspot.lat!!, hotspot.lng!!)
                    val markerOptions =
                        MarkerOptions().position(LatLng(hotspot.lat!!, hotspot.lng!!))
                            .title(hotspot.locName)

                    map.addMarker(markerOptions)
                }

                map.moveCamera(CameraUpdateFactory.newLatLng(p))
                //TODO: Add a circle around the users current location
                val circleOptions = CircleOptions().center(p).radius(10000.0) // Radius in meters
                    .strokeColor(Color.BLUE)
                map.addCircle(circleOptions)

                // TODO: Move to where we are on the map.

                map.setOnMarkerClickListener { marker ->
                    val position = marker.position
                    // Move to the next fragment and pass in the position
//                    (activity as LoginActivity).loadFragment(RouteBirding(position))

                    Bundle().apply {
                        putDouble("lat", position.latitude)
                        putDouble("lng", position.longitude)
                    }.also { bundle ->
                        findNavController().navigate(R.id.navigation_route, bundle)
                    }



                    //                val markerTitle = marker.title
                    //                val markerSnippet = marker.snippet
                    //                println(markerTitle)
                    true
                }



                map.moveCamera(CameraUpdateFactory.zoomTo(11F));
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            } else MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                .setMessage(it.exceptionOrNull()?.message ?: "Unknown error").setCancelable(true)
                .show()

        }
    }

    private fun hasLocationPermissions(): Boolean {
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val fineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return coarseLocationPermission && fineLocationPermission
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSIONS_CODE
        )
    }

}
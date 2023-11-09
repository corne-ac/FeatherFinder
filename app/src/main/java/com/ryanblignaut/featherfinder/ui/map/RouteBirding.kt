package com.ryanblignaut.featherfinder.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.ryanblignaut.featherfinder.R

import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.RouteViewModel


class RouteBirding : PreBindingFragment<FragmentMapBinding>(),
    OnMapReadyCallback {


    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetContent: LinearLayout
    private lateinit var routeViewModel: RouteViewModel
    private var positionIn = LatLng(0.0, 0.0)
    override fun addContentToView(savedInstanceState: Bundle?) {


        

        positionIn = LatLng(
            arguments?.getDouble("lat") ?: 0.0,
            arguments?.getDouble("lng") ?: 0.0
        )
        routeViewModel = ViewModelProvider(this)[RouteViewModel::class.java]

        val googleMap =
            childFragmentManager.findFragmentById(com.ryanblignaut.featherfinder.R.id.map) as SupportMapFragment?
        googleMap?.onCreate(savedInstanceState)
        googleMap?.getMapAsync(this);

        // Set up BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(), R.style.ThemeOverlay_Catalog_BottomSheetDialog_Scrollable
        )

        bottomSheetDialog.setContentView(R.layout.cat_bottomsheet_scrollable_content)
        val bottomSheetInternal =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        BottomSheetBehavior.from<View>(bottomSheetInternal!!).peekHeight = 400
        bottomSheetDialog.setTitle("Route Information")
        bottomSheetDialog.show()
        bottomSheetContent = bottomSheetInternal.findViewById(R.id.bottom_drawer_2)

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentMapBinding {
        return inflateBinding(inflater, container)
    }

    override fun onMapReady(map: GoogleMap) {

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
            return onMapReady(map)
        }
        map.isMyLocationEnabled = true

        /*  try {
              // Customise the styling of the base map using a JSON object defined
              // in a raw resource file.
              val success: Boolean = map.setMapStyle(
                  MapStyleOptions.loadRawResourceStyle(
                      requireContext(), com.ryanblignaut.featherfinder.R.raw.style_json
                  )
              )
              if (!success) {
                  Log.e("e", "Style parsing failed.")
              }
          } catch (e: Resources.NotFoundException) {
              Log.e("a", "Can't find style. Error: ", e)
          }*/
//        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val currentLocation = LatLng(location.latitude, location.longitude)
            val startLat = currentLocation.latitude
            val startLong = currentLocation.longitude
            val endLat = positionIn.latitude
            val endLong = positionIn.longitude


            Log.d("FF_APP", "onMapReady: $startLat, $startLong, $endLat, $endLong")
            routeViewModel.fetchRoute(endLong, endLat, startLong, startLat)
            map.addMarker(MarkerOptions().position(positionIn))
        }

        routeViewModel.live.observe(viewLifecycleOwner) {
            if (it.isSuccess) {

                val jsonObject = it.getOrNull()!!
//                jsonObject.getJSONObject("features").getJSONObject("properties").getJSONObject("segments").getJSONObject("steps")
                val featuresArray = jsonObject.getJSONArray("features")
                val firstFeature = featuresArray.getJSONObject(0)
                val properties = firstFeature.getJSONObject("properties")
                val segments = properties.getJSONArray("segments")
                for (i in 0 until segments.length()) {
                    val segment = segments.getJSONObject(i)
                    val steps = segment.getJSONArray("steps")
                    for (j in 0 until steps.length()) {
                        val step = steps.getJSONObject(j)
                        Log.d("FF_APP", "Instruction: ${step.getString("instruction")}")
                        val textView = TextView(
                            ContextThemeWrapper(
                                requireContext(),
                                R.style.selectableTextView
                            )
                        )
                        textView.height = resources.getDimensionPixelSize(R.dimen.text)
                        textView.text = step.getString("instruction")
                        bottomSheetContent.addView(textView)
                    }
                }


                val layer = GeoJsonLayer(map, jsonObject)

                layer.defaultLineStringStyle.color = 0xFF0000FF.toInt()
                layer.addLayerToMap()
                layer.defaultLineStringStyle.width = 15f
                layer.boundingBox?.let { bounds ->
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                }

            } else {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                    .setMessage(it.exceptionOrNull()?.message ?: "Unknown error")
                    .setCancelable(true).show()
            }
        }
    }
}
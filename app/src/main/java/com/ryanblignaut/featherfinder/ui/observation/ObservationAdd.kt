package com.ryanblignaut.featherfinder.ui.observation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentObservationAddBinding
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.DataValidator
import com.ryanblignaut.featherfinder.viewmodel.helper.FormState
import com.ryanblignaut.featherfinder.viewmodel.helper.FormStateNew
import com.ryanblignaut.featherfinder.viewmodel.observation.AddObservationViewModel

/**
 * This class represents the user interface for creating a bird observation.
 * It provides functionality to record details of a bird sighting made by a user.
 * Bird observations include information such as the bird species, location, date, and additional notes.
 */
class ObservationAdd : PreBindingFragment<FragmentObservationAddBinding>() {
    private val formViewModel: AddObservationViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient //Used to get user location
    private lateinit var locationCallback: LocationCallback //Used for location services

    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.saveObservationAction.setOnClickListener { saveObservation() }
        formViewModel.live.observe(viewLifecycleOwner, ::onSaveObservationResult)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Do all the form state stuff here to prevent ghost calls.
        val formStates = listOf(
            speciesNameState(),
            dateState(),
            timeState()
        )
        // Attach the listeners to the form states.
        formStates.forEach(FormState::attachListener)
        // Observe the form state.
        formViewModel.formState.observe(viewLifecycleOwner, updateFormStates(formStates))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext()) //initialise for retrieving location later
    }

    private fun onSaveObservationResult(result: Result<String>) {
        if (result.isFailure) {
            TODO("Error message")
        }

        findNavController().navigate(R.id.navigation_observation_list)
    }

    private fun updateFormStates(formStates: List<FormState>): (MutableMap<String, String?>) -> Unit {
        return {
            // Validate the form states.
            formStates.forEach(FormState::validate)
            // If all form states are valid, enable the login button.
            binding.saveObservationAction.isEnabled = formStates.all(FormState::isValid)
        }
    }

    private fun speciesNameState(): FormState {
        return FormStateNew(
            binding.birdSpecies,
            "speciesName",
            formViewModel,
            DataValidator::speciesNameValidation,
        )
    }

    private fun dateState(): FormState {
        return FormStateNew(
            binding.date,
            "date",
            formViewModel,
            DataValidator::dateValidation,
        )
    }

    private fun timeState(): FormState {
        return FormStateNew(
            binding.time,
            "time",
            formViewModel,
            DataValidator::timeValidation,
        )
    }

    @SuppressLint("MissingPermission")
    private fun saveObservation() {
        //get user location then pass to save
        if (binding.myLocSwitch.isChecked) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? -> //Can return null
                    if (location != null) {
                        val userLoc = LatLng(location.latitude, location.longitude)
                        val lat = userLoc.latitude.toString()
                        val long = userLoc.longitude.toString()
                        //Save here
                        formViewModel.saveObservation(
                            BirdObservation(
                                binding.birdSpecies.getText(),
                                binding.date.getText(),
                                binding.time.getText(),
                                binding.notes.getText(),
                                lat,
                                long
                            )
                        )
                    } else {
                        //Try to start location service, as it is most common cause for a null return
                        locServiceTry()
                    }

                }
                .addOnFailureListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Location Retrieval Error")
                        .setMessage(it.message ?: "Unknown error")
                        .setCancelable(true)
                        .show()
                }
        } else {
            formViewModel.saveObservation(
                BirdObservation(
                    binding.birdSpecies.getText(),
                    binding.date.getText(),
                    binding.time.getText(),
                    binding.notes.getText(),
                    "",
                    ""
                )
            )
        }


    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentObservationAddBinding {
        return inflateBinding(inflater, container)
    }

    @SuppressLint("MissingPermission")
    private fun locServiceTry() {
        // 1. Start the location service
        val locationRequest = LocationRequest.Builder(10000) // 10 seconds
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // 100
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // 3. Use the location for further processing
                    val userLoc = LatLng(location.latitude, location.longitude)
                    //Save here
                    formViewModel.saveObservation(
                        BirdObservation(
                            binding.birdSpecies.getText(),
                            binding.date.getText(),
                            binding.time.getText(),
                            binding.notes.getText(),
                            userLoc.latitude.toString(),
                            userLoc.longitude.toString()
                        )
                    )

                    // Stop location updates after the first successful retrieval
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }
                //Dialog should only show if fail
//                // Show a dialog indicating that no location was found
//                MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("Location Service Error")
//                    .setMessage("Please enable a location service on device")
//                    .setCancelable(true)
//                    .show()
            }
        }
    }
}
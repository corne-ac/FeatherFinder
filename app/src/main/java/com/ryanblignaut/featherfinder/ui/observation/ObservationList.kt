package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.databinding.FragmentObservationListBinding
import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.observation.AllObservationsViewModel

/**
 * This class represents the user interface for viewing a bird observations.
 * It provides functionality by providing a list of bird sighting made by a user.
 * The list items will be able to be clicked in order to show more details of the sighting.
 */
class ObservationList : PreBindingFragment<FragmentObservationListBinding>() {
    private val model: AllObservationsViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.loader.visibility = ViewGroup.VISIBLE
        model.live.observe(viewLifecycleOwner, ::populateObservationList)
        model.getObservations()
        binding.addObservationAction.setOnClickListener {
            findNavController().navigate(ObservationListDirections.actionNavigationObservationListToNavigationAddObservation())
        }
    }

    private fun populateObservationList(result: Result<List<BirdObservation>>) {
        binding.loader.visibility = ViewGroup.GONE

        if (result.isFailure) {
            val throwable = result.exceptionOrNull()!!
            if (throwable is FirebaseDataManager.ItemNotFoundExceptionFirebase) {
                binding.noItemsFound.visibility = ViewGroup.VISIBLE
                return
            }
            throwable.printStackTrace()
            TODO("Show error message")
            return
        }
        val values = result.getOrNull()!!
        binding.observationsRecyclerView.adapter =
            ObservationListViewAdapter(values, ::onObservationClick)

    }

    private fun onObservationClick(observation: BirdObservation) {
        val detailNav =
            ObservationListDirections.actionObservationListToObservationDetail(observation.id)
        findNavController().navigate(detailNav)
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentObservationListBinding {
        return inflateBinding(inflater, container)
    }


}
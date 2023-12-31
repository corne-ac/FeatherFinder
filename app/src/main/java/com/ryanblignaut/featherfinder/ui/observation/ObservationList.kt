package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.databinding.FragmentObservationListBinding
import com.ryanblignaut.featherfinder.model.BirdObsTitle
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

        binding.filterSortObservations.setOnClickListener {

            val dialog = ObservationSortFilterDialog(::getObsSortFilter)

            dialog.show(parentFragmentManager, "sortFilterDialog")
        }

        binding.addObservationAction.setOnClickListener {
            findNavController().navigate(ObservationListDirections.actionNavigationObservationListToNavigationAddObservation())
        }
    }

    private fun populateObservationList(result: Result<List<BirdObsTitle>?>) {
        binding.loader.visibility = ViewGroup.GONE

        if (result.isFailure) {
            val throwable = result.exceptionOrNull()!!
            throwable.printStackTrace()
            MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                .setMessage(throwable.localizedMessage).setCancelable(true)
                .show()
            return
        }
        val values = result.getOrNull()!!
        if (values.isEmpty()) {
            binding.noItemsFound.visibility = ViewGroup.VISIBLE
            return
        }

        binding.observationsRecyclerView.adapter =
            ObservationListViewAdapter(values, ::onObservationClick)

    }

    private fun onObservationClick(observation: BirdObsTitle) {
        val detailNav = ObservationListDirections.actionObservationListToObservationDetail(
            observation.id, observation.date, observation.birdSpecies
        )
        findNavController().navigate(detailNav)
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentObservationListBinding {
        return inflateBinding(inflater, container)
    }

    fun getObsSortFilter(filterTime: String, nameSort: Boolean) {

        model.getObservationsSorted(filterTime, nameSort)

    }

}
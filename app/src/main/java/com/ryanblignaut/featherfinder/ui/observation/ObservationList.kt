package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentObservationItemListBinding
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.observation.AllObservationsViewModel

/**
 * This class represents the user interface for viewing a bird observations.
 * It provides functionality by providing a list of bird sighting made by a user.
 * The list items will be able to be clicked in order to show more details of the sighting.
 */
class ObservationList : PreBindingFragment<FragmentObservationItemListBinding>() {
    private var columnCount = 1
    private val model: AllObservationsViewModel by viewModels()


    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.loader.visibility = ViewGroup.VISIBLE
        model.live.observe(viewLifecycleOwner, ::populateObservationList)
        model.getObservations()
        binding.addObservationAction.setOnClickListener { findNavController().navigate(R.id.navigation_add_observation) }


        /*   // When the recycler view is ready, set the layout manager.
           with(recyclerView) {
               layoutManager = when {
                   columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                   else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
               }

               // Populate the on click listener for the recycler view items.
               fun openObservationDetails(it: String) {

               }

               // TODO: get the items from local storage or from db.
               adapter = ObservationListViewAdapter(ObservationPlaceholderData.ITEMS) {
                   openObservationDetails(it.id)
               }
           }*/
    }

    private fun populateObservationList(result: Result<List<BirdObservation>>) {
        binding.loader.visibility = ViewGroup.GONE
        if (result.isFailure) {
            TODO("Show error message")
        }
        val observations = result.getOrNull()!!
        if (observations.isEmpty()) binding.noObservationsText.visibility = ViewGroup.VISIBLE
        binding.observationsRecyclerView.adapter =
            ObservationListViewAdapter(observations, ::onObservationClick)

    }

    private fun onObservationClick(observation: BirdObservation) {
        println("Clicked on goal")
        println(observation.id)
        TODO("On click of observation, open observation details")
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentObservationItemListBinding {
        return inflateBinding(inflater, container)
    }


}
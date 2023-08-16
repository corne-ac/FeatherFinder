package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentObservationItemListBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.ui.observation.placeholder.ObservationPlaceholderData

/**
 * This class represents the user interface for viewing a bird observations.
 * It provides functionality by providing a list of bird sighting made by a user.
 * The list items will be able to be clicked in order to show more details of the sighting.
 */
class ObservationList : PreBindingFragment<FragmentObservationItemListBinding>() {
    private var columnCount = 1


    override fun addContentToView(savedInstanceState: Bundle?) {
        val recyclerView = binding.observationRecyclerView

        // When the recycler view is ready, set the layout manager.
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
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentObservationItemListBinding {
        return inflateBinding(inflater, container)
    }


}
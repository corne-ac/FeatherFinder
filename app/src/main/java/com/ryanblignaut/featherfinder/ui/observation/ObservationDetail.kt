package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentObservationDetailBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

/**
 * This class represents the user interface for viewing a bird observations.
 * It provides functionality by providing more details of bird sighting made by a user.
 * The list items will be able to be clicked in order to show more details of the sighting.
 */
class ObservationDetail : PreBindingFragment<FragmentObservationDetailBinding>() {
    override fun addContentToView(savedInstanceState: Bundle?) {

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): FragmentObservationDetailBinding {
        return inflateBinding(inflater, container)
    }

}
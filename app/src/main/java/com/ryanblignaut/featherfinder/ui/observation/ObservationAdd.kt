package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentObservationAddBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

/**
 * This class represents the user interface for creating a bird observation.
 * It provides functionality to record details of a bird sighting made by a user.
 * Bird observations include information such as the bird species, location, date, and additional notes.
 */
class ObservationAdd : PreBindingFragment<FragmentObservationAddBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentObservationAddBinding {
        return inflateBinding(inflater, container)
    }

}
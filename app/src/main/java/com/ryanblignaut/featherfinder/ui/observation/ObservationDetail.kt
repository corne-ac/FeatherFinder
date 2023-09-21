package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        formViewModel.getObservationById(args.observationId)
        binding.birdSpecies.setText(args.birdSpecies)
        binding.date.setText(args.date)
        formViewModel.live.observe(viewLifecycleOwner, ::loadObservationDetails)
    }

    private fun loadObservationDetails(result: Result<BirdObsDetails?>) {

        if (result.isFailure) {
            val throwable = result.exceptionOrNull()!!
            throwable.printStackTrace()
        }
        val values = result.getOrNull()!!
        binding.time.setText(values.time)
        binding.pos.setText(values.location)
        binding.notes.setText(values.notes)

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): FragmentObservationAddBinding {
        return inflateBinding(inflater, container)
    }

}
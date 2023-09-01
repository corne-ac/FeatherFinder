package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentObservationAddBinding
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.DataValidator
import com.ryanblignaut.featherfinder.viewmodel.helper.FormState
import com.ryanblignaut.featherfinder.viewmodel.observation.AddObservationViewModel

/**
 * This class represents the user interface for creating a bird observation.
 * It provides functionality to record details of a bird sighting made by a user.
 * Bird observations include information such as the bird species, location, date, and additional notes.
 */
class ObservationAdd : PreBindingFragment<FragmentObservationAddBinding>() {
    private val formViewModel: AddObservationViewModel by viewModels()

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
            timeState(),
            positionState(),
        )
        // Attach the listeners to the form states.
        formStates.forEach(FormState::attachListener)
        // Observe the form state.
        formViewModel.formState.observe(viewLifecycleOwner, updateFormStates(formStates))
    }

    private fun onSaveObservationResult(result: Result<BirdObservation>) {
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
        return FormState(
            binding.birdSpecies,
            binding.birdSpeciesInputLayout,
            "speciesName",
            formViewModel,
            DataValidator::speciesNameValidation,
        )
    }

    private fun dateState(): FormState {
        return FormState(
            binding.date,
            binding.dateInputLayout,
            "date",
            formViewModel,
            DataValidator::dateValidation,
        )
    }

    private fun timeState(): FormState {
        return FormState(
            binding.time,
            binding.timeInputLayout,
            "time",
            formViewModel,
            DataValidator::timeValidation,
        )
    }

    private fun positionState(): FormState {
        return FormState(
            binding.pos,
            binding.posInputLayout,
            "position",
            formViewModel,
            DataValidator::positionValidation,
        )
    }


    private fun saveObservation() {
        formViewModel.saveObservation(
            BirdObservation(
                binding.birdSpecies.text.toString(),
                binding.date.text.toString(),
                binding.time.text.toString(),
                binding.pos.text.toString(),
                binding.notes.text.toString()
            )
        )
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentObservationAddBinding {
        return inflateBinding(inflater, container)
    }

}
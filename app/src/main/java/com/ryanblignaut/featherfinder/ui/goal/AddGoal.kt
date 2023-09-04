package com.ryanblignaut.featherfinder.ui.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.databinding.FragmentGoalAddBinding
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.DataValidator
import com.ryanblignaut.featherfinder.viewmodel.goal.GoalViewModel
import com.ryanblignaut.featherfinder.viewmodel.helper.FormState

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class AddGoal : PreBindingFragment<FragmentGoalAddBinding>() {
    private val formViewModel: GoalViewModel by viewModels()
    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.saveGoal.setOnClickListener { saveGoal() }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Do all the form state stuff here to prevent ghost calls.
        val formStates = listOf(
            goalNameState(),
        )
        // Attach the listeners to the form states.
        formStates.forEach(FormState::attachListener)
        // Observe the form state.
        formViewModel.formState.observe(viewLifecycleOwner, updateFormStates(formStates))
    }

    private fun saveGoal() {
        formViewModel.saveGoal(
            Goal(
                name = binding.goalName.text.toString(),
                startTime = binding.start.text.toString(),
                endTime = binding.end.text.toString(),
                description = binding.goalInfo.text.toString(),
            )
        )
    }

    private fun updateFormStates(formStates: List<FormState>): (value: MutableMap<String, String?>) -> Unit {
        return {
            // Validate the form states.
            formStates.forEach(FormState::validate)
            // If all form states are valid, enable the login button.
            binding.saveGoal.isEnabled = formStates.all(FormState::isValid)
        }
    }

    private fun goalNameState(): FormState {
        return FormState(
            binding.goalName,
            binding.goalNameInputLayout,
            "goalName",
            formViewModel,
            DataValidator::goalNameValidation,
        )
    }


    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentGoalAddBinding {
        return inflateBinding(inflater, container)
    }
}
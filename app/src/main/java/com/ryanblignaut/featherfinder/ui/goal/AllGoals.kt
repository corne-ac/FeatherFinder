package com.ryanblignaut.featherfinder.ui.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentAllGoalsBinding
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.goal.AllGoalsViewModel

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class AllGoals : PreBindingFragment<FragmentAllGoalsBinding>() {
    private val model: AllGoalsViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Start the loader.
        binding.loader.visibility = ViewGroup.VISIBLE
        model.live.observe(viewLifecycleOwner, ::populateGoalList)
        model.getGoals()
        binding.addGoalAction.setOnClickListener { findNavController().navigate(R.id.navigation_add_goal) }
    }

    private fun populateGoalList(it: Result<List<Goal>>) {
        // Stop the loading animation
        binding.loader.visibility = ViewGroup.GONE
        if (it.isFailure) {
            // TODO: Show error message
            println("We have no goals")
            println(it.exceptionOrNull())
            return
        }
        val values = it.getOrNull()!!
        if (values.isEmpty()) {
            binding.noGoalsText.visibility = ViewGroup.VISIBLE
        }
        binding.goalsRecyclerView.adapter = GoalAdapter(values, ::onGoalClick)
    }

    private fun onGoalClick(goal: Goal) {
        println("Clicked on goal")
        println(goal.id)
        //TODO: Open the goal view fragment
    }


    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentAllGoalsBinding {
        return inflateBinding(inflater, container)
    }
}
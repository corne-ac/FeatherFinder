package com.ryanblignaut.featherfinder.ui.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentGoalListBinding
import com.ryanblignaut.featherfinder.model.Fullgoal
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.goal.AllGoalsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class AllGoals : PreBindingFragment<FragmentGoalListBinding>() {
    private val model: AllGoalsViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Start the loader.
//        binding.loader.visibility = ViewGroup.VISIBLE
        binding.loadingRecyclerView.showLoading()
        model.live.observe(viewLifecycleOwner, ::populateGoalList)
        model.getGoals()
        binding.addGoalAction.setOnClickListener {
//            findNavController().navigate(R.id.navigation_add_goal)
            findNavController().navigate(R.id.action_all_goals_to_add_goal)

        }
    }

    private fun populateGoalList(it: Result<List<Fullgoal>?>) {
        if (it.isFailure) {
            // TODO: Show error message
            println("We have no goals")
            println(it.exceptionOrNull())
            return
        }
        val values = it.getOrNull()!!
        if (values.isEmpty()) {
            binding.loadingRecyclerView.showEmptyText()
        }
        binding.loadingRecyclerView.setAdapter(GoalAdapter(values, ::onGoalClick, findNavController()))
    }

    private fun onGoalClick(goal: Fullgoal) {
        println("Clicked on goal")
        println(goal.id)
        //TODO: Open the goal view fragment
    }


    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentGoalListBinding {
        return inflateBinding(inflater, container)
    }


}
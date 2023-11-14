package com.ryanblignaut.featherfinder.ui.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentGoalListBinding
import com.ryanblignaut.featherfinder.model.FullGoal
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.goal.AllGoalsViewModel


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

        // Had to hack this in to move back else user is stuck on the profile page.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigation_profile)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


    }

    private fun populateGoalList(it: Result<List<FullGoal>?>) {
        if (it.isFailure) {
            println("We have no goals")
            println(it.exceptionOrNull())
            return
        }
        val values = it.getOrNull()!!
        if (values.isEmpty()) {
            binding.loadingRecyclerView.showEmptyText()
        }
        binding.loadingRecyclerView.setAdapter(
            GoalAdapter(
                values, ::onGoalDeleteClick, ::onGoalTickClick
            )
        )
    }

    private fun onGoalTickClick(goal: FullGoal, vh: GoalAdapter.ViewHolder) {
        if (!goal.goalCompleted) {
            model.completeGoal(goal.selfId)
            vh.imgCheck.setImageResource(R.drawable.bird_mint)

        } else {
            model.removeCompletionOnGoal(goal.selfId)
            vh.imgCheck.setImageResource(R.drawable.check)
        }
    }

    private fun onGoalDeleteClick(goal: FullGoal) {
        model.deleteGoal(goal.selfId)
        model.getGoals()
    }


    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentGoalListBinding {
        return inflateBinding(inflater, container)
    }
}
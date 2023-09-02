package com.ryanblignaut.featherfinder.ui.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.databinding.FragmentRegisterBinding
import com.ryanblignaut.featherfinder.model.Achievement
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.achievement.AllAchievementsViewModel

class ViewAchievements : PreBindingFragment<FragmentRegisterBinding>() {
    private val model: AllAchievementsViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        model.live.observe(viewLifecycleOwner, ::populateAchievementsList)
//        model.getAchievements()
    }

    private fun populateAchievementsList(result: Result<List<Achievement>>) {
        if (result.isFailure) {
            TODO("Show the error message for this")
        }
        AchievementAdapter(result.getOrNull()!!, ::onAchievementClick)
    }

    private fun onAchievementClick(achievement: Achievement) {
        TODO("Move to the achievement view fragment")
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentRegisterBinding {
        return inflateBinding(inflater, container)
    }
}
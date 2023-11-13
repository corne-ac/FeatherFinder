package com.ryanblignaut.featherfinder.ui.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.databinding.FragmentAllAchievementsBinding
import com.ryanblignaut.featherfinder.model.AchievementManager
import com.ryanblignaut.featherfinder.model.UserAchievement
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.achievement.AllAchievementsViewModel

class ViewAchievements : PreBindingFragment<FragmentAllAchievementsBinding>() {
    private val model: AllAchievementsViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        model.live.observe(viewLifecycleOwner, ::populateAchievementsList)
        model.getAchievements()
    }

    private fun populateAchievementsList(result: Result<UserAchievement?>) {
        if (result.isFailure) {

        }
        // If this is null then all the achievements are locked.
        val userAchievement = result.getOrNull()
        // This list contains all the achievements and the isUnlocked will determine if it is unlocked.
        val usersAchievements = AchievementManager.getUsersAchievements(userAchievement)

        // Split into 2 lists one with unlocked and one with locked.
        val unlockedAchievements = usersAchievements.filter { it.isUnlocked }
        val lockedAchievements = usersAchievements.filter { !it.isUnlocked }

        // Now we create the adapter with the unlocked achievements first and then the locked achievements.
        val unlockedAdapter = AchievementAdapter(unlockedAchievements)
        val lockedAdapter = AchievementAdapter(lockedAchievements)

        // Now we set the adapters to the recycler views.
        binding.achievementsUnlockedRecyclerView.adapter = unlockedAdapter
        binding.achievementsLockedRecyclerView.adapter = lockedAdapter

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentAllAchievementsBinding {
        return inflateBinding(inflater, container)
    }
}
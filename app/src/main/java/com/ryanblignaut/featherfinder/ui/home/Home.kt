package com.ryanblignaut.featherfinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ryanblignaut.featherfinder.databinding.FragmentHomeBinding
import com.ryanblignaut.featherfinder.model.FullGoal
import com.ryanblignaut.featherfinder.model.UserAchievement
import com.ryanblignaut.featherfinder.model.UserSettings
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class Home : PreBindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        viewModel.updateLoginStatus()
        viewModel.liveAchievement.observe(viewLifecycleOwner, ::populateAchievements)
        viewModel.getUserSettings()
        viewModel.live.observe(viewLifecycleOwner, ::updateDarkTheme)
        viewModel.updateUrgentGoal()
        viewModel.urgentGoal.observe(viewLifecycleOwner, ::populateUrgentGoal)
    }

    private fun populateUrgentGoal(urgentGoal: Result<FullGoal?>) {
        urgentGoal.getOrNull()?.let {
            binding.upcomingGoal.text = it.name
            return
        }
        binding.upcomingGoal.text = "No upcoming goal"
    }

    private fun updateDarkTheme(settingsResult: Result<UserSettings?>) {
        val isDarkMode = settingsResult.getOrNull()?.isDarkMode == true
        // Post night mode change operation to the UI thread
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    private fun unixTimeToLocalDate(unixTime: Long): LocalDate {
        val instant = Instant.ofEpochSecond(unixTime)
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun populateAchievements(userAchievementResult: Result<UserAchievement?>) {
        val achievement = userAchievementResult.getOrNull()
        if (achievement != null) {
            binding.dailyAchievementCount.text = achievement.loginDaysStreak.toString()
            binding.goalCompletedCount.text = achievement.totalGoalsComp.toString()
            binding.birdCaptureCount.text = achievement.totalBirdsSeen.toString()
        } else {
            binding.dailyAchievementCount.text = "0"
            binding.goalCompletedCount.text = "0"
            binding.birdCaptureCount.text = "0"
        }

        if (achievement?.lastLoginTime == null) {
            viewModel.updateLoginDays(0)
        } else {
            val newLogin = LocalDate.now()
            val lastLogin = unixTimeToLocalDate(achievement.lastLoginTime)
            if (newLogin.isAfter(lastLogin.plusDays(1))) {
                // Here we do the day streak counter
                val loginDaysStreak = achievement.loginDaysStreak
                if (newLogin.isAfter(lastLogin.plusDays(2))) {
                    // Reset the streak
                    viewModel.updateLoginDays(0)
                } else {
                    viewModel.updateLoginDays(loginDaysStreak + 1)
                }
                // Refresh the achievement info.
                viewModel.updateLoginStatus()
            }
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): FragmentHomeBinding {
        return inflateBinding(inflater, container)
    }


}
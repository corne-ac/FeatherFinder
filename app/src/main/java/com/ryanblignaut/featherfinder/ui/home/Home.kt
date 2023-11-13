package com.ryanblignaut.featherfinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ryanblignaut.featherfinder.databinding.FragmentHomeBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class Home : PreBindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.dailyAchievementCount.text = ""
        binding.goalCompletedCount.text = ""
        binding.birdCaptureCount.text = ""
        viewModel.updateLoginStatus()

        viewModel.liveAchievement.observe(viewLifecycleOwner) {
            val achievement = it.getOrNull()
            if (achievement != null) {
                binding.dailyAchievementCount.text = achievement.loginDaysStreak.toString()
                binding.goalCompletedCount.text = achievement.totalGoalsComp.toString()
                binding.birdCaptureCount.text = achievement.totalBirdsSeen.toString()
            } else {
                binding.dailyAchievementCount.text = "0"
                binding.goalCompletedCount.text = "0"
                binding.birdCaptureCount.text = "0"
            }

            val newLogin = LocalDate.now()
            val lastLogin = LocalDate.ofEpochDay(achievement?.lastLoginTime ?: 0)
            if (newLogin.isAfter(lastLogin.plusDays(1))) {
                // Here we do the day streak counter
                val loginDaysStreak = achievement?.loginDaysStreak ?: 0
                if (newLogin.isAfter(lastLogin.plusDays(2))) {
                    // Reset the streak
                    viewModel.updateLoginStatus()
                } else {
                    viewModel.updateLoginDays(loginDaysStreak + 1)

                }
            }
        }

        viewModel.getUserSettings()
        viewModel.live.observe(viewLifecycleOwner) {
            val isDarkMode = it.getOrNull()?.isDarkMode == true
            // Post night mode change operation to the UI thread
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
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
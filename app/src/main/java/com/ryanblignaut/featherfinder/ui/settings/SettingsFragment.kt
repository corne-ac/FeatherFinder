package com.ryanblignaut.featherfinder.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentSetBinding
import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.model.UserSettings
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.SettingReferences
import com.ryanblignaut.featherfinder.viewmodel.settings.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsFragment : PreBindingFragment<FragmentSetBinding>() {
    private val viewModel: SettingsViewModel by viewModels()
    override fun addContentToView(savedInstanceState: Bundle?) {

        viewModel.getUserSettings()

        val metricOptions = arrayOf("Metric", "Imperial")
        binding.measurement.setAdapter(
            ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, metricOptions
            )
        )
        binding.measurement.setOnItemClickListener { _, _, position, _ ->
            SettingReferences.userSettings.isMetric = position == 0
        }

        val darkModeOptions = arrayOf("Off", "On")
        binding.darkMode.setAdapter(
            ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, darkModeOptions
            )
        )
        binding.darkMode.setOnItemClickListener { _, _, position, _ ->

            SettingReferences.userSettings.isDarkMode = position == 1
        }
//        binding.darkMode.setText(darkModeOptions[0], false)
        var units = " Miles "
        if (SettingReferences.userSettings.isMetric) units = " Km "
        val map = SettingReferences.MaxDist.entries.map {
            it.maxDist.toString() + units
        }

        val rangeOptions = map.toTypedArray()
        binding.distance.setAdapter(
            ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, rangeOptions
            )
        )
        binding.distance.setOnItemClickListener { _, _, position, _ ->
            SettingReferences.userSettings.maxDistance = position
        }
//        binding.distance.setText(rangeOptions[0], false)

        binding.saveSettingsAction.setOnClickListener {
            viewModel.updateSettings(SettingReferences.userSettings)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                if (SettingReferences.userSettings.isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            findNavController().navigate(R.id.navigation_home)


        }
        viewModel.live.observe(viewLifecycleOwner, ::updateSettings)

        // Here we check if the user is verified and if not we show the button to send the verification email.
        Log.d(
            "SettingsFragment",
            "isEmailVerified: ${FirebaseAuthManager.getCurrentUser()!!.isEmailVerified}"
        )

        if (FirebaseAuthManager.getCurrentUser()!!.isEmailVerified) {
            binding.verifyEmail.isEnabled = false
            binding.emailStatus.text = "Email Verified"
        }
        binding.verifyEmail.setOnClickListener {
            FirebaseAuthManager.sendVerificationEmail()
        }

        binding.refresh.setOnClickListener {
            FirebaseAuthManager.getCurrentUser()!!.reload().addOnSuccessListener {
                    if (FirebaseAuthManager.getCurrentUser()!!.isEmailVerified) {
                        binding.verifyEmail.isEnabled = false
                        binding.emailStatus.text = "Email Verified"
                    }
                }
        }


    }

    private fun updateSettings(result: Result<UserSettings?>) {
        if (result.isFailure) return
        if (result.getOrNull() == null) return
        val settings = result.getOrNull()!!


        binding.measurement.setText(if (settings.isMetric) "Metric" else "Imperial", false)
        binding.darkMode.setText(if (settings.isDarkMode) "On" else "Off", false)
        var units = " Miles "
        if (settings.isMetric) units = " Km "
        binding.distance.setText(
            SettingReferences.MaxDist.values()[settings.maxDistance].maxDist.toString() + units,
            false
        )
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentSetBinding {
        return inflateBinding(inflater, container)
    }
}
package com.ryanblignaut.featherfinder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ryanblignaut.featherfinder.databinding.FragmentSetBinding
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
                requireContext(),
                android.R.layout.simple_list_item_1,
                metricOptions
            )
        )
        binding.measurement.setOnItemClickListener { _, _, position, _ ->
            SettingReferences.userSettings.isMetric = position == 0
        }
        binding.measurement.setText(metricOptions[0], false)

        val darkModeOptions = arrayOf("Off", "On")
        binding.darkMode.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                darkModeOptions
            )
        )
        binding.darkMode.setOnItemClickListener { _, _, position, _ ->

            SettingReferences.userSettings.isDarkMode = position == 1
        }
        binding.darkMode.setText(darkModeOptions[0], false)


        val rangeOptions = arrayOf("1 Km", "5 Km", "10 Km", "50 Km")
        binding.distance.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                rangeOptions
            )
        )
        binding.distance.setOnItemClickListener { _, _, position, _ ->
            //TODO: map this to the actual distance
            SettingReferences.userSettings.maxDistance = position
        }
        binding.distance.setText(rangeOptions[0], false)

        binding.saveSettingsAction.setOnClickListener {
            viewModel.updateSettings(SettingReferences.userSettings)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                if (SettingReferences.userSettings.isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        }
        viewModel.live.observe(viewLifecycleOwner, ::updateSettings)

    }

    private fun updateSettings(result: Result<UserSettings?>) {
        if (result.isFailure) return
        if (result.getOrNull() == null) return
        val settings = result.getOrNull()!!


        binding.measurement.setText(if (settings.isMetric) "Metric" else "Imperial", false)
        binding.darkMode.setText(if (settings.isDarkMode) "On" else "Off", false)
        binding.distance.setText(settings.maxDistance.toString() + " Km ", false)
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentSetBinding {
        return inflateBinding(inflater, container)
    }
}
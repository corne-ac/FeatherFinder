package com.ryanblignaut.featherfinder.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentObservationListBinding
import com.ryanblignaut.featherfinder.databinding.FragmentSetBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class SettingsFragment : PreBindingFragment<FragmentSetBinding>()
    {

//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.root_preferences, rootKey)
//        PreferenceManager.getDefaultSharedPreferences(requireContext())
//            .registerOnSharedPreferenceChangeListener(this)
//    }
//
//    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        val darkModeString = getString(R.string.dark_mode)
//        key?.let {
//            if (it == darkModeString) sharedPreferences?.let { pref ->
//                val darkModeValues = resources.getStringArray(R.array.dark_mode_values)
//                when (pref.getString(darkModeString, darkModeValues[0])) {
//                    darkModeValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                    darkModeValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    darkModeValues[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    darkModeValues[3] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
//                }
//            }
//        }
//    }

    override fun addContentToView(savedInstanceState: Bundle?) {
        //TODO("Not yet implemented")
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentSetBinding {
        return inflateBinding(inflater, container)
    }

//    override fun onDestroy() {
//        PreferenceManager.getDefaultSharedPreferences(requireContext())
//            .unregisterOnSharedPreferenceChangeListener(this)
//        super.onDestroy()
//    }

}
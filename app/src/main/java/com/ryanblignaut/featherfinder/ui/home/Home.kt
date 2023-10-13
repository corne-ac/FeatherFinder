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

class Home : PreBindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
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
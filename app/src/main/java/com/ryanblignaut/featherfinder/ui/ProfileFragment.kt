package com.ryanblignaut.featherfinder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentProfileBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class ProfileFragment : PreBindingFragment<FragmentProfileBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {

        binding.settingsAction.setOnClickListener {
//            findNavController().navigate(R.id.navigation_settings)
            TODO("Implement settings screen")
        }
        binding.goalsAction.setOnClickListener {
            findNavController().navigate(R.id.navigation_all_goals)
        }
        binding.achievementsAction.setOnClickListener {
            findNavController().navigate(R.id.navigation_achievement)
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentProfileBinding {
        return inflateBinding(inflater, container)
    }

}
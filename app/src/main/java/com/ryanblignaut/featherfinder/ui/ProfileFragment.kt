package com.ryanblignaut.featherfinder.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ryanblignaut.featherfinder.LoginActivity
import com.ryanblignaut.featherfinder.MainActivity
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentProfileBinding
import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class ProfileFragment : PreBindingFragment<FragmentProfileBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {

        binding.settingsAction.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_settingsFragment)
            //TODO("Implement settings screen")
        }
        binding.goalsAction.setOnClickListener {
            findNavController().navigate(R.id.navigation_all_goals)
        }
        binding.achievementsAction.setOnClickListener {
            findNavController().navigate(R.id.navigation_achievement)
        }

        binding.logoutAction.setOnClickListener {
            FirebaseAuthManager.signOutOwn()

            this.activity?.finish()
            Intent(this.activity, LoginActivity::class.java).setAction(Intent.ACTION_VIEW)
                .also { intent ->
                    this.activity?.startActivity(intent)
                }
        }

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentProfileBinding {
        return inflateBinding(inflater, container)
    }

}
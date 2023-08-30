package com.ryanblignaut.featherfinder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.SettingsActivity
import com.ryanblignaut.featherfinder.databinding.FragmentLoginBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.LoginViewModel

/**
 * This class represents the user interface for a user to login.
 * Login include user information including username, email, password and confirm password.
 */
class Login : PreBindingFragment<FragmentLoginBinding>() {
    private lateinit var viewModel: LoginViewModel

    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.register.setOnClickListener {
            // TODO: Navigate with findNavController and set up graph showing nav
            (requireActivity() as SettingsActivity).loadFragment(Register())
        }
        binding.forgotPassword.setOnClickListener {
            (requireActivity() as SettingsActivity).loadFragment(ForgotPassword())
        }

        binding.login.isEnabled = true

        binding.login.setOnClickListener {
//            (requireActivity() as SettingsActivity).loadFragment(SettingsFragment())
//            (requireActivity() as SettingsActivity).loadFragment(NearbyBirding())

            // Move to the activity with the buttons on the bottom.

        }

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): FragmentLoginBinding {
        return inflateBinding(inflater, container)
    }


}
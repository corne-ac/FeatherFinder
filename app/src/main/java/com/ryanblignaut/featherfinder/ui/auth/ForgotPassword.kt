package com.ryanblignaut.featherfinder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentForgotPasswordBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.LoginViewModel

/**
 * This class represents the user interface for a user to login.
 * Login include user information including username, email, password and confirm password.
 */
class ForgotPassword : PreBindingFragment<FragmentForgotPasswordBinding>() {
    private lateinit var viewModel: LoginViewModel

    override fun addContentToView(savedInstanceState: Bundle?) {
        /*  binding.register.setOnClickListener {
              // TODO: Navigate with findNavController and set up graph showing nav
  //            findNavController().navigate(com.ryanblignaut.featherfinder.R.id.mobile_navigation)
              (requireActivity() as SettingsActivity).loadFragment(Register())
          }*/

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentForgotPasswordBinding {
        return inflateBinding(inflater, container)
    }


}
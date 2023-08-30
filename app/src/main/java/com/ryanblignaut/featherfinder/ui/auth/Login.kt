package com.ryanblignaut.featherfinder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.MainDrawerNav
import com.ryanblignaut.featherfinder.MainMenuActivity
import com.ryanblignaut.featherfinder.SettingsActivity
import com.ryanblignaut.featherfinder.databinding.FragmentLoginBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.LoginViewModel

/**
 * This class represents the user interface for a user to login.
 * Login include user information including username, email, password and confirm password.
 */
class Login : PreBindingFragment<FragmentLoginBinding>() {
    private val formViewModel: LoginViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Create these variables to store the email and password.
        // Assigned to null as they are not yet initialized and we don't want to immediately harass the user.
        var email: String? = null
        var password: String? = null

        binding.email.doAfterTextChanged {
            email = it.toString()
            formViewModel.dataChanged(email, password)
        }
        binding.password.doAfterTextChanged {
            password = it.toString()
            formViewModel.dataChanged(email, password)
        }
        formViewModel.loginFormState.observe(viewLifecycleOwner) {
            binding.login.isEnabled = it.isDataValid
            binding.emailInputLayout.error = it.emailError?.let { err -> getString(err) }
            binding.passwordInputLayout.error = it.passwordError?.let { err -> getString(err) }
        }

        binding.forgotPassword.setOnClickListener {
            (requireActivity() as SettingsActivity).loadFragment(ForgotPassword())
        }

        binding.register.setOnClickListener {
            (requireActivity() as SettingsActivity).loadFragment(Register())
        }

        binding.login.setOnClickListener {
            formViewModel.login(email!!, password!!)
        }

        formViewModel.live.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                // Open the main activity.
                // This is the main activity that will be opened when the user logs in.
                this.activity?.finish()
                Intent(this.activity, MainMenuActivity::class.java).also { intent ->
                    this.activity?.startActivity(intent)
                }
            } else {
                // Display the error message to the user.
            }

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
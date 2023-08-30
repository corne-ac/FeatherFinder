package com.ryanblignaut.featherfinder.ui.auth

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.SettingsActivity
import com.ryanblignaut.featherfinder.databinding.FragmentRegisterBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.RegisterViewModel

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class Register : PreBindingFragment<FragmentRegisterBinding>() {


    // Nice by viewModels automatically creates the view model for us at the right time with the right context.
    private val formViewModel: RegisterViewModel by viewModels()
    override fun addContentToView(savedInstanceState: Bundle?) {
        // Create these variables to store the username, email, password and confirm password.
        // Assigned to null as they are not yet initialized and we don't want to immediately harass the user.
        var username: String? = null
        var email: String? = null
        var password: String? = null
        var confirmPassword: String? = null

        binding.username.doAfterTextChanged {
            username = it.toString()
            formViewModel.dataChanged(username, email, password, confirmPassword)
        }

        binding.email.doAfterTextChanged {
            email = it.toString()
            formViewModel.dataChanged(username, email, password, confirmPassword)
        }

        binding.password.doAfterTextChanged {
            password = it.toString()
            formViewModel.dataChanged(username, email, password, confirmPassword)
        }
        val action: (text: Editable?) -> Unit = {
            confirmPassword = it.toString()
            formViewModel.dataChanged(username, email, password, confirmPassword)
        }
        binding.passwordConfirm.doAfterTextChanged(action)
        formViewModel.registerFormState.observe(viewLifecycleOwner) {
            binding.register.isEnabled = it.isDataValid
            binding.usernameInputLayout.error = it.usernameError?.let { err -> getString(err) }
            binding.emailInputLayout.error = it.emailError?.let { err -> getString(err) }
            binding.passwordInputLayout.error = it.passwordError?.let { err -> getString(err) }
            binding.passwordConfirmInputLayout.error =
                it.confirmPasswordError?.let { err -> getString(err) }
        }

        binding.login.setOnClickListener {
            (requireActivity() as SettingsActivity).loadFragment(Login())
        }

        binding.register.setOnClickListener {
            formViewModel.registerUserInFirebase(email!!, password!!)
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentRegisterBinding {
        return inflateBinding(inflater, container)
    }
}
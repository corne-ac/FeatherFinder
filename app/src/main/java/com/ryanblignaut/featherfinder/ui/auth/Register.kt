package com.ryanblignaut.featherfinder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.databinding.FragmentRegisterBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.RegisterViewModel

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class Register : PreBindingFragment<FragmentRegisterBinding>() {

    // Nice by viewModels automatically creates the view model for us at the right time with the right context.
    private val viewModel: RegisterViewModel by viewModels()
    override fun addContentToView(savedInstanceState: Bundle?) {
        binding.email.doAfterTextChanged {
            viewModel.email = it.toString()
            viewModel.validateEmail()
            binding.email.error = viewModel.validationErrors.value?.emailError
        }

        binding.password.doAfterTextChanged {
            viewModel.password = it.toString()
            viewModel.validatePassword()
            binding.password.error = viewModel.validationErrors.value?.passwordError
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentRegisterBinding {
        return inflateBinding(inflater, container)
    }
}
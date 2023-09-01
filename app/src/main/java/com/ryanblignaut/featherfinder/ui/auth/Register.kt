package com.ryanblignaut.featherfinder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.MainActivity
import com.ryanblignaut.featherfinder.databinding.FragmentRegisterBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.DataValidator
import com.ryanblignaut.featherfinder.viewmodel.auth.RegisterViewModel
import com.ryanblignaut.featherfinder.viewmodel.helper.FormState

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class Register : PreBindingFragment<FragmentRegisterBinding>() {

    // Nice by viewModels automatically creates the view model for us at the right time with the right context.
    private val formViewModel: RegisterViewModel by viewModels()
    override fun addContentToView(savedInstanceState: Bundle?) {
        // Build up the form state with the fields we have on the page.
        val formStates = listOf(
            usernameState(),
            emailState(),
            passwordState(),
            confirmPasswordState(),
        )
        // Attach the listeners to the form states.
        formStates.forEach(FormState::attachListener)
        // Observe the form state.
        formViewModel.formState.observe(viewLifecycleOwner, updateFormStates(formStates))

        binding.register.setOnClickListener {
            formViewModel.register(
                binding.username.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString(),
            )
        }

        formViewModel.live.observe(viewLifecycleOwner, ::onRegisterResult)

    }

    private fun onRegisterResult(result: Result<FirebaseUser>) {
        if (result.isFailure) {
            TODO("Error this out " + result.exceptionOrNull()!!.message)
        }
        this.activity?.finish()
        Intent(this.activity, MainActivity::class.java).setAction(Intent.ACTION_VIEW)
            .also { intent ->
                this.activity?.startActivity(intent)
            }

    }

    private fun updateFormStates(formStates: List<FormState>): (value: MutableMap<String, String?>) -> Unit {
        return {
            // Validate the form states.
            formStates.forEach(FormState::validate)
            // If all form states are valid, enable the register button.
            binding.register.isEnabled = formStates.all(FormState::isValid)
        }
    }

    private fun usernameState(): FormState {
        return FormState(
            binding.username,
            binding.usernameInputLayout,
            "username",
            formViewModel,
            DataValidator::usernameValidation,
        )
    }

    private fun emailState(): FormState {
        return FormState(
            binding.email,
            binding.emailInputLayout,
            "email",
            formViewModel,
            DataValidator::emailValidation,
        )
    }

    private fun passwordState(): FormState {
        return FormState(
            binding.password,
            binding.passwordInputLayout,
            "password",
            formViewModel,
            DataValidator::passwordValidation,
        )
    }

    private fun confirmPasswordState(): FormState {
        return FormState(
            binding.passwordConfirm,
            binding.passwordConfirmInputLayout,
            "confirmPassword",
            formViewModel,
        ) { DataValidator.confirmPasswordValidation(it, binding.password.text?.toString()) }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentRegisterBinding {
        return inflateBinding(inflater, container)
    }
}
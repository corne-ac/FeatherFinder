package com.ryanblignaut.featherfinder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.ryanblignaut.featherfinder.MainMenuActivity
import com.ryanblignaut.featherfinder.SettingsActivity
import com.ryanblignaut.featherfinder.databinding.FragmentLoginBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.DataValidator
import com.ryanblignaut.featherfinder.viewmodel.FormState
import com.ryanblignaut.featherfinder.viewmodel.LoginView2Model

/**
 * This class represents the user interface for a user to login.
 * Login include user information including username, email, password and confirm password.
 */
class Login : PreBindingFragment<FragmentLoginBinding>() {
    //    private val formViewModel: LoginView2Model by viewModels()
//    private lateinit var formViewModel: LoginView2Model
//    private val formViewModel: LoginView2Model by viewModels()
    private val formViewModel: LoginView2Model by viewModels { ViewModelProvider.NewInstanceFactory() }

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Build up the form state.
        val formStates = listOf(
            FormState(
                binding.email,
                binding.emailInputLayout,
                DataValidator::emailValidation,
                "email",
                formViewModel
            ),
            FormState(
                binding.password,
                binding.passwordInputLayout,
                DataValidator::passwordValidation,
                "password",
                formViewModel
            ),
        )

        // Attach the listeners to the form states.
        formStates.forEach(FormState::attachListener)

        formViewModel.loginFormState.observe(viewLifecycleOwner) {
            // Validate the form states.
            formStates.forEach(FormState::validate)
            // If all form states are valid, enable the login button.
            binding.login.isEnabled = formStates.all(FormState::isValid)
        }

        binding.forgotPassword.setOnClickListener {
            (requireActivity() as SettingsActivity).loadFragment(ForgotPassword())
        }

        binding.register.setOnClickListener {
            (requireActivity() as SettingsActivity).loadFragment(Register())
        }

        binding.login.setOnClickListener {
            // TODO: put into a component.
            val spec = CircularProgressIndicatorSpec(
                requireContext(),  /*attrs=*/
                null, 0, getSpecStyleResId()
            )
            val progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(
                requireContext(), spec
            )
            spec.indicatorColors = intArrayOf(
                requireContext().getColor(android.R.color.holo_blue_light),
                requireContext().getColor(android.R.color.holo_green_light),
                requireContext().getColor(android.R.color.holo_orange_light),
                requireContext().getColor(android.R.color.holo_red_light)
            )
            binding.login.icon = progressIndicatorDrawable
            binding.login.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
            formViewModel.login(binding.email.text.toString(), binding.password.text.toString())
            binding.login.text = ""
            binding.login.isEnabled = false
        }

        formViewModel.live.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                // Open the main activity.
                // This is the main activity that will be opened when the user logs in.
                this.activity?.finish()
                Intent(this.activity, MainMenuActivity::class.java).setAction(Intent.ACTION_VIEW)
                    .also { intent ->
                        this.activity?.startActivity(intent)
                    }
            } else {
                // Display the error message to the user.
            }
        }
    }


    @StyleRes
    private fun getSpecStyleResId(): Int {
        return com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): FragmentLoginBinding {
        return inflateBinding(inflater, container)
    }


}
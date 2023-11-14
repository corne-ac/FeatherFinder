package com.ryanblignaut.featherfinder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.firebase.auth.FirebaseUser
import com.ryanblignaut.featherfinder.LoginActivity
import com.ryanblignaut.featherfinder.MainActivity
import com.ryanblignaut.featherfinder.databinding.FragmentLoginBinding
import com.ryanblignaut.featherfinder.firebase.FirebaseAuthManager
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.DataValidator
import com.ryanblignaut.featherfinder.viewmodel.auth.LoginViewModel
import com.ryanblignaut.featherfinder.viewmodel.helper.FormState

/**
 * This class represents the user interface for a user to login.
 * Login include user information including username, email, password and confirm password.
 */
class Login : PreBindingFragment<FragmentLoginBinding>() {
    private val formViewModel: LoginViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Firebase Seems to keep sessions so we can check if the user is already logged in.
        FirebaseAuthManager.getCurrentUser()?.let {
            moveToMain()
        }



        binding.forgotPassword.setOnClickListener {
            (requireActivity() as LoginActivity).loadFragment(ForgotPassword())
        }

        binding.register.setOnClickListener {
            (requireActivity() as LoginActivity).loadFragment(Register())
        }

        binding.loginAction.setOnClickListener {
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
            binding.loginAction.icon = progressIndicatorDrawable
            binding.loginAction.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
            formViewModel.login(binding.email.text.toString(), binding.password.text.toString())
            binding.loginAction.text = ""
            binding.loginAction.isEnabled = false
        }

        formViewModel.live.observe(viewLifecycleOwner, ::onLoginResult)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Build up the form state.
        val formStates = listOf(
            emailState(),
            passwordState(),
        )

        // Attach the listeners to the form states.
        formStates.forEach(FormState::attachListener)
        formViewModel.formState.observe(viewLifecycleOwner, updateFormStates(formStates))

    }


    private fun onLoginResult(result: Result<FirebaseUser>) {
        if (result.isFailure) {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                .setMessage("Invalid credentials").setCancelable(true)
                .show()

            binding.loginAction.isEnabled = true
            binding.loginAction.icon = null
            return
        }
        moveToMain()
    }

    private fun moveToMain() {
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
            // If all form states are valid, enable the login button.
            binding.loginAction.isEnabled = formStates.all(FormState::isValid)
        }
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
package com.ryanblignaut.featherfinder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentLoginBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.LoginViewModel

/**
 * This class represents the user interface for a user to login.
 * Login include user information including email and password as well as remember me.
 */
class Login : PreBindingFragment<FragmentLoginBinding>() {
    private lateinit var viewModel: LoginViewModel

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentLoginBinding {
        return inflateBinding(inflater, container)

    }


}
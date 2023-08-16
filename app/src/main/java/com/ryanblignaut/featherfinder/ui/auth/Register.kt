package com.ryanblignaut.featherfinder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentRegisterBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

/**
 * This class represents the user interface for a user to register.
 * It provides functionality to record user details including username, email, password, confirm password.
 */
class Register : PreBindingFragment<FragmentRegisterBinding>() {


    override fun addContentToView(savedInstanceState: Bundle?) {

    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentRegisterBinding {
        return inflateBinding(inflater, container)
    }
}
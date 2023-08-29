package com.ryanblignaut.featherfinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentHomeBinding
import com.ryanblignaut.featherfinder.databinding.FragmentLoginBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class Home : PreBindingFragment<FragmentHomeBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentHomeBinding {
        return inflateBinding(inflater, container)
    }


}
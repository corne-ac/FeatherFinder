package com.ryanblignaut.featherfinder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentProfileBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class ProfileFragment : PreBindingFragment<FragmentProfileBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentProfileBinding {
        return inflateBinding(inflater, container)
    }

}
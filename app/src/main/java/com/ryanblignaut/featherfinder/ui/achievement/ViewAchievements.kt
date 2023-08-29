package com.ryanblignaut.featherfinder.ui.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentRegisterBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class ViewAchievements : PreBindingFragment<FragmentRegisterBinding>() {
    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentRegisterBinding {
        return inflateBinding(inflater, container)
    }
}
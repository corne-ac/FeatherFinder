package com.ryanblignaut.featherfinder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentBirdInfoBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class BirdInfoFragment : PreBindingFragment<FragmentBirdInfoBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentBirdInfoBinding {
        return inflateBinding(inflater, container)
    }

}
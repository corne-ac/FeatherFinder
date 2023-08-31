package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ryanblignaut.featherfinder.databinding.FragmentObsBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment

class ObsFragment : PreBindingFragment<FragmentObsBinding>() {

    override fun addContentToView(savedInstanceState: Bundle?) {
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentObsBinding {
        return inflateBinding(inflater, container)
    }

}
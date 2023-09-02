package com.ryanblignaut.featherfinder.ui.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ryanblignaut.featherfinder.databinding.FragmentBirdInfoBinding
import com.ryanblignaut.featherfinder.model.api.XenoResponse
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.BirdInfoViewModel

class BirdInfoFragment : PreBindingFragment<FragmentBirdInfoBinding>() {

    private val viewModel: BirdInfoViewModel by viewModels()

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Look at https://medium.com/@shashankmohabia/android-paging-efficient-way-to-populate-recycler-view-dynamically-31f39f35cdf9
        // for how to implement paging
        binding.loader.visibility = ViewGroup.VISIBLE
        viewModel.live.observe(viewLifecycleOwner, ::populateBirdList)
        viewModel.fetchBirdsInLocation("Cape")
    }

    private fun populateBirdList(result: Result<XenoResponse>) {
        binding.loader.visibility = ViewGroup.GONE

        if (result.isFailure) {
            val throwable = result.exceptionOrNull()!!
            throwable.printStackTrace()
            return
        }
        val response = result.getOrNull()!!
        val birds = response.recordings
        binding.birdingRecyclerView.adapter = InfoAdapter(birds) { bird ->
            val file = bird.file
            println(file)
        }
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentBirdInfoBinding {
        return inflateBinding(inflater, container)
    }

}
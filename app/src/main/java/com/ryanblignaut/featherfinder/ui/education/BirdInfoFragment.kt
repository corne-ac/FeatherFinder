package com.ryanblignaut.featherfinder.ui.education

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.databinding.FragmentBirdInfoListBinding
import com.ryanblignaut.featherfinder.model.api.XenoRecording
import com.ryanblignaut.featherfinder.model.api.XenoResponse
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.BirdInfoViewModel

class BirdInfoFragment : PreBindingFragment<FragmentBirdInfoListBinding>() {

    private val viewModel: BirdInfoViewModel by viewModels()
    private var mediaPair: Pair<MediaPlayer, InfoAdapter.ViewHolder>? = null

    override fun addContentToView(savedInstanceState: Bundle?) {
        // Look at https://medium.com/@shashankmohabia/android-paging-efficient-way-to-populate-recycler-view-dynamically-31f39f35cdf9
        // for how to implement paging
        binding.loadingRecyclerView.showLoading()
        viewModel.live.observe(viewLifecycleOwner, ::populateBirdList)
        viewModel.fetchBirdsInLocation("centurion")
    }

    private fun populateBirdList(result: Result<XenoResponse>) {

        if (result.isFailure) {
            val throwable = result.exceptionOrNull()!!
            throwable.printStackTrace()
            binding.loadingRecyclerView.hideLoading()
            return
        }
        val values = result.getOrNull()!!.recordings


        val filteredBirds = values.distinctBy { it.en }.filter { it.file != null }.toList()


        val adapter = InfoAdapter(filteredBirds, ::onBirdClicked)
        binding.loadingRecyclerView.setAdapter(adapter)
    }

    private fun onBirdClicked(holder: InfoAdapter.ViewHolder, recording: XenoRecording) {
        // Current media playing
        if (mediaPair != null) {
            val media = mediaPair!!.first

            media.stop()
//            media.release()
            mediaPair!!.second.playButton.setImageResource(android.R.drawable.ic_media_play)
            if (mediaPair!!.second == holder) {
                return
            }
        }




        mediaPair = Pair(MediaPlayer(), holder)
        val media = mediaPair!!.first
        media.setDataSource(recording.file)
        media.prepareAsync()

        media.setOnPreparedListener {
            // Loader.End()
            mediaPair!!.second.playButton.setImageResource(android.R.drawable.ic_media_pause)
            media.start()
        }
        media.setOnCompletionListener {
            media.release()
        }


        /*
         if (recording.file == null) {
             return
         }
         currentMedia.setDataSource(recording.file)
         currentMedia.prepareAsync()


         holder.playButton.setImageResource(android.R.drawable.ic_media_pause)
         currentMedia.setOnPreparedListener {
             currentMedia.start()
         }
         currentMedia.setOnCompletionListener {
             currentMedia.release()
         }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPair?.first?.release()
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentBirdInfoListBinding {
        return inflateBinding(inflater, container)
    }

}
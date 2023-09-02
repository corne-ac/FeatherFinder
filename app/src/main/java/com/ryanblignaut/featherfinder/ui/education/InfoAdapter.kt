package com.ryanblignaut.featherfinder.ui.education

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentBirdInfoItemBinding
import com.ryanblignaut.featherfinder.model.api.XenoRecording

class InfoAdapter(
    private val values: List<XenoRecording>,
    private val onClick: (XenoRecording) -> Unit,
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            FragmentBirdInfoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.heading.text = item.en
        holder.playButton.setOnClickListener { onClick(item) }
    }

    inner class ViewHolder(binding: FragmentBirdInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val heading: TextView = binding.headingTextView
        val playButton: ImageView = binding.playAction
    }

}
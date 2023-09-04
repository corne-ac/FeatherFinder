package com.ryanblignaut.featherfinder.ui.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentObservationItemBinding
import com.ryanblignaut.featherfinder.model.Achievement

class AchievementAdapter(
    private val values: List<Achievement>,
    private val onClick: (Achievement) -> Unit,
) : RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            FragmentObservationItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
//        holder.idView.text = item.id
//        holder.contentView.text = item.description
//        holder.contentView.setOnClickListener { onClick(item) }
    }

    inner class ViewHolder(binding: FragmentObservationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
//        val contentView: TextView = binding.content

    }

}
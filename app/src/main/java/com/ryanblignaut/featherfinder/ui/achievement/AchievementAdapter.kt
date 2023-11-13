package com.ryanblignaut.featherfinder.ui.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentAchievementInfoItemBinding
import com.ryanblignaut.featherfinder.model.Achievement

class AchievementAdapter(
    private val values: List<Achievement>,
) : RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            FragmentAchievementInfoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.details.text = item.description
        holder.name.text = item.name
        if (!item.isUnlocked)
            holder.image.setColorFilter(com.ryanblignaut.featherfinder.R.color.gray)

    }

    inner class ViewHolder(binding: FragmentAchievementInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val details = binding.achievementDetail
        val name = binding.achievementName
        val image = binding.achievementIcon
//        val idView: TextView = binding.itemNumber
//        val contentView: TextView = binding.content

    }

}
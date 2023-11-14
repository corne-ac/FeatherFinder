package com.ryanblignaut.featherfinder.ui.goal

import android.graphics.Color
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentGoalItemBinding
import com.ryanblignaut.featherfinder.model.FullGoal
import java.util.Date
import java.util.concurrent.TimeUnit

class GoalAdapter(
    private val values: List<FullGoal>,
    private val onDelClick: (FullGoal) -> Unit,
    private val onTicClick: (FullGoal, ViewHolder) -> Unit,
) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            FragmentGoalItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.detail.text = item.description
        holder.imgCheck.setOnClickListener {
                onTicClick(item, holder)
                item.goalCompleted = !item.goalCompleted
            }
        holder.imgDelete.setOnClickListener { onDelClick(item) }

        if (item.goalCompleted) {
            // TODO: REal icon
            holder.imgCheck.setColorFilter(Color.GREEN)
            holder.imgDelete.setColorFilter(Color.RED)
        } else {
            holder.imgCheck.setColorFilter(Color.DKGRAY)
        }

        // Added a check to ensure that we have both dates before we try to compare them
        if (item.startTime != -1L && item.endTime != -1L) {
            val durationInMillis = item.endTime - Date().time
            // Change end-date colour based on how close it is. 1 day = orange, 7 days = yellow, more = green, past = red.
            val daysBetween =
                TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS).toInt();
            holder.daysLeft.text = getDaysLeftText(daysBetween)
            //Change colour based on how close it is. 1 day = orange, 7 days = yellow, more = green, past = red
            holder.iconDaysLeft.setColorFilter(getColor(daysBetween))
        }
    }

    private fun getDaysLeftText(daysBetween: Int): String {
        return when {
            daysBetween < 0 -> "Passed ${-daysBetween} days ago"
            daysBetween == 0 -> "Today"
            else -> "$daysBetween days left"
        }
    }

    private fun getColor(daysBetween: Int): Int {
        if (daysBetween < 0) return Color.RED
        if (daysBetween <= 1) return 0xFFFFA500.toInt()
        if (daysBetween <= 7) return Color.YELLOW
        return Color.GREEN

    }

    inner class ViewHolder(binding: FragmentGoalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val container: View = binding.goalContainer
        val name = binding.goalName
        val detail = binding.goalDetail
        val imgCheck = binding.imgCheck
        val imgDelete = binding.imgDelete
        val daysLeft = binding.daysLeft
        val iconDaysLeft = binding.iconDaysLeft
    }

}
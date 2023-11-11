package com.ryanblignaut.featherfinder.ui.goal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentGoalItemBinding
import com.ryanblignaut.featherfinder.model.Fullgoal
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.GoalTitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GoalAdapter(
    private val values: List<Fullgoal>,
    private val onClick: (Fullgoal) -> Unit,
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
//        holder.idView.text = item.id
//        holder.contentView.text = item.description
//        holder.contentView.setOnClickListener { onClick(item) }

        holder.name.text = item.name
        holder.detail.text = item.description
        holder.startDate.text = item.startTime
        holder.endDate.text = item.endTime

        //Change enddate colour based on how close it is. 1 day = orange, 7 days = yellow, more = green, past = red

        //TODO: Consider saying "X Days Left" rather than showing dates with colour

        val todayString = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH).format(Date())
        val inbetween = getDaysBetween(todayString, item.endTime)
        holder.endDate.setBackgroundColor(getColor(inbetween))
        holder.endDate.setTextColor(Color.BLACK)
        holder.container.setOnClickListener { onClick(item) }
    }

    private fun getColor(inbetween: Int): Int {
        if (inbetween < 0)
            return Color.RED
        if (inbetween <= 1)
            return 0xFFFFA500.toInt()
        if (inbetween <= 7)
            return Color.YELLOW
        return Color.GREEN

    }

    inner class ViewHolder(binding: FragmentGoalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
//        val contentView: TextView = binding.content

        /*override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/

        val container : View = binding.goalContainer
        val name = binding.goalName
        val detail = binding.goalDetail
        val startDate = binding.startDate
        val endDate = binding.endDate
    }

    fun getDaysBetween(startDateString: String, endDateString: String): Int {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val startDate: Date = dateFormat.parse(startDateString)!!
        val endDate: Date = dateFormat.parse(endDateString)!!
        val durationInMillis: Long = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS).toInt()
    }

}
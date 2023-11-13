package com.ryanblignaut.featherfinder.ui.goal

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentGoalItemBinding
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.Fullgoal
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.GoalTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GoalAdapter(
    private val values: List<Fullgoal>,
    private val onClick: (Fullgoal) -> Unit,
    private val navController: NavController,
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

        holder.imgCheck.setOnClickListener {  }
        holder.imgDelete.setOnClickListener {
//            GlobalScope.launch(Dispatchers.Main) {
//                    val result = FirestoreDataManager.deleteGoal(item.id)
//
//                    navController.navigate(R.id.navigation_all_goals)
//            }
        }



        //Change enddate colour based on how close it is. 1 day = orange, 7 days = yellow, more = green, past = red

        val todayString = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH).format(Date())
        val inbetween = getDaysBetween(todayString, item.endTime)
        holder.daysLeft.text = getDaysLeftText(inbetween)
        //build text based on days left

        holder.iconDaysLeft.setColorFilter(getColor(inbetween))

        holder.container.setOnClickListener { onClick(item) }
    }

    private fun getDaysLeftText(inbetween: Int): String {
        return when {
            inbetween < 0 -> "Passed ${-inbetween} days ago"
            inbetween == 0 -> "Today"
            else -> "${inbetween} days left"
        }
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
        val imgCheck = binding.imgCheck
        val imgDelete = binding.imgDelete
        val daysLeft = binding.daysLeft
        val iconDaysLeft = binding.iconDaysLeft
    }

    fun getDaysBetween(startDateString: String, endDateString: String): Int {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val startDate: Date = dateFormat.parse(startDateString)!!
        val endDate: Date = dateFormat.parse(endDateString)!!
        val durationInMillis: Long = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS).toInt()
    }

}
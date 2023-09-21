package com.ryanblignaut.featherfinder.ui.goal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentGoalItemBinding
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.GoalTitle

class GoalAdapter(
    private val values: List<GoalTitle>,
    private val onClick: (GoalTitle) -> Unit,
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
        holder.container.setOnClickListener { onClick(item) }
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
    }

}
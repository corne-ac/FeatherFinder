package com.ryanblignaut.featherfinder.ui.goal

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentObservationItemBinding
import com.ryanblignaut.featherfinder.model.Goal

class GoalAdapter(
    private val values: List<Goal>,
    private val onClick: (Goal) -> Unit,
) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {
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
        holder.idView.text = item.id
        holder.contentView.text = item.birdSpecies
        holder.contentView.setOnClickListener { onClick(item) }
    }

    inner class ViewHolder(binding: FragmentObservationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}
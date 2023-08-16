package com.ryanblignaut.featherfinder.ui.observation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentObservationItemBinding
import com.ryanblignaut.featherfinder.model.BirdObservation

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ObservationListViewAdapter(
    private val values: MutableList<BirdObservation>,
    private val onClick: (BirdObservation) -> Unit,
) : RecyclerView.Adapter<ObservationListViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentObservationItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.birdSpecies
        holder.contentView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentObservationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}
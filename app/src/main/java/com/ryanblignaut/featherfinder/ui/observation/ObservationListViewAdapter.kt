package com.ryanblignaut.featherfinder.ui.observation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryanblignaut.featherfinder.databinding.FragmentObservationItemBinding
import com.ryanblignaut.featherfinder.model.BirdObservation

class ObservationListViewAdapter(
    private val values: List<BirdObservation>,
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
        holder.name.text = item.birdSpecies
        holder.details.text = item.date
        holder.container.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentObservationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.birdName
        val details: TextView = binding.birdDetails
        val container: View = binding.observationItemCardContainer

    }

}
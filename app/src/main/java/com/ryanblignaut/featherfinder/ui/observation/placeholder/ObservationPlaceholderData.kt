package com.ryanblignaut.featherfinder.ui.observation.placeholder

import com.ryanblignaut.featherfinder.model.BirdObservation

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object ObservationPlaceholderData {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<BirdObservation> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, BirdObservation> = HashMap()

    private const val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i))
        }
    }

    private fun addItem(item: BirdObservation) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createPlaceholderItem(position: Int): BirdObservation {
        return BirdObservation(
            position.toString(), "Item $position", "", "Loc 1 ", makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }
}
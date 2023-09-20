package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.ui.component.ext.setOnClickListener
import com.ryanblignaut.featherfinder.ui.component.ext.show


class TimePickerInput : TextInput {
    private lateinit var picker: MaterialTimePicker

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {
        initControl(context, attrs)
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private fun initControl(context: Context, attrs: AttributeSet?) {
        // Inflation is done in the TextInput constructor.
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TimePickerInput)
        binding.value.inputType = 0
        binding.value.isFocusable = false
        binding.value.setOnClickListener(::showPicker)
        picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        attributes.recycle()
    }

    private fun showPicker() {
        picker.addOnPositiveButtonClickListener { onSaveClicked() }
        picker.show(context, "TimePicker")
    }

    private fun onSaveClicked() {
        // Add padding to the hour if it is a single digit.
        val selectedHour = picker.hour.toString().padStart(2, '0')
        // Add padding to the minute if it is a single digit.
        val selectedMinute = picker.minute.toString().padStart(2, '0')
        // Create a string that combines the hour and minute.
        val selectedTime = "$selectedHour:$selectedMinute"
        // Set the text view text.
        binding.value.setText(selectedTime)
    }
}


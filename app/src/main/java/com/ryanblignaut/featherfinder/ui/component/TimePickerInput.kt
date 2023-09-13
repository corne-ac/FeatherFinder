package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ryanblignaut.featherfinder.R


class TimePickerInput : TextInput {
    private var picker: MaterialTimePicker? = null

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

    private fun showPicker(it: View) {
        val fragmentActivity: FragmentActivity? = tryGetFragmentActivity()
        if (fragmentActivity != null) {
            picker?.addOnPositiveButtonClickListener(::onSaveClicked)
            picker?.show(fragmentActivity.supportFragmentManager, "TimePicker")
        } else {
            // TODO: Handle this case
            // Handle the case where FragmentActivity is not found
        }
    }

    // TODO: maybe move to util class
    // Attempt to find the FragmentActivity
    private fun tryGetFragmentActivity(): FragmentActivity? {
        var fragmentActivity: FragmentActivity? = null
        var context = this.context
        // Context seems to be androidx.appcompat.view.ContextThemeWrapper but that extends ContextWrapper so this should be safe.
        while (context is ContextWrapper) {
            if (context is FragmentActivity) {
                fragmentActivity = context
                break
            }
            context = context.baseContext
        }
        return fragmentActivity
    }


    private fun onSaveClicked(it: View) {
        // Add padding to the hour if it is a single digit.
        val selectedHour = picker!!.hour.toString().padStart(2, '0')
        // Add padding to the minute if it is a single digit.
        val selectedMinute = picker!!.minute.toString().padStart(2, '0')
        // Create a string that combines the hour and minute.
        val selectedTime = "$selectedHour:$selectedMinute"
        // Set the text view text.
        binding.value.setText(selectedTime)
    }
}
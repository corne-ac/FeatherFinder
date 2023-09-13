package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.ryanblignaut.featherfinder.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DatePickerInput : TextInput {
    private lateinit var picker: MaterialDatePicker<Long>

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {
        initControl(context, attrs)
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private fun initControl(context: Context, attrs: AttributeSet?) {
        // Inflation is done in the TextInput constructor.
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DatePickerInput)
        binding.value.inputType = 0
        binding.value.isFocusable = false
        binding.value.setOnClickListener(::showPicker)
        picker = MaterialDatePicker.Builder.datePicker().build()
        attributes.recycle()
    }

    private fun showPicker(it: View) {
        val fragmentActivity: FragmentActivity? = tryGetFragmentActivity()
        if (fragmentActivity != null) {
            picker.addOnPositiveButtonClickListener(::onSaveClicked)
            if (!picker.isAdded) picker.show(fragmentActivity.supportFragmentManager, "TimePicker")
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

    private fun onSaveClicked(it: Long) {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH)
        binding.value.setText(sdf.format(Date(it)).toString())
    }
}
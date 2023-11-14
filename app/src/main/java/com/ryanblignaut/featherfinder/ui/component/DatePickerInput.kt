package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.datepicker.MaterialDatePicker
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.ui.component.ext.show
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
        picker.addOnPositiveButtonClickListener(::onSaveClicked)
        picker.show(context)
    }

    private fun onSaveClicked(it: Long) {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH)
        binding.value.setText(sdf.format(Date(it)).toString())
    }
}
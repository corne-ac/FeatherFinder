package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputLayout
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.ComponentTextInputBinding


open class TextInput : TextInputLayout {
    lateinit var binding: ComponentTextInputBinding

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {
        initControl(context, attrs)
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private fun initControl(context: Context, attrs: AttributeSet?) {
        val inflater = LayoutInflater.from(context)
        // Inflate and bind to the route if parent is not null.
        binding = ComponentTextInputBinding.inflate(inflater, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TextInput)
        attributes.getString(R.styleable.TextInput_hint)?.let { binding.valueInputLayout.hint = it }
        attributes.getResourceId(R.styleable.TextInput_icon, 0)
            .let(binding.valueInputLayout::setStartIconDrawable)
        attributes.getDimension(R.styleable.TextInput_compHeight, 0f)
            .let { binding.value.minimumHeight = it.toInt() }

        attributes.getBoolean(R.styleable.TextInput_large, false)
            .let {
                if (it) {
                    //<flag name="textMultiLine" value="0x00020001" />
                    binding.value.inputType = 0x00020001
                }
            }

        attributes.recycle()
    }

    public fun getText(): String {
        return binding.value.text.toString()
    }

    private fun setText(text: String) {
        binding.value.setText(text)
    }

    private fun setError(error: String) {

    }


}
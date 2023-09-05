package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.ComponentCardButtonBinding

class ButtonCardView : MaterialCardView {

    private lateinit var binding: ComponentCardButtonBinding

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
        binding = ComponentCardButtonBinding.inflate(inflater, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ButtonCardView)

        setImage(attributes)
        setText(attributes)


        attributes.recycle()
    }

    private fun setText(attributes: TypedArray) {
        attributes.getString(R.styleable.ButtonCardView_label)?.let {
            binding.cardText.text = it
        }
    }

    private fun setImage(attributes: TypedArray) {
        val resourceId = attributes.getResourceId(R.styleable.ButtonCardView_src_img, -1)
        if (resourceId != -1) {
            binding.cardImage.setImageResource(resourceId)
        }
    }

}
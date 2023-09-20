package com.ryanblignaut.featherfinder.ui.component.ext

import android.view.View

fun View.setOnClickListener(action: () -> Unit) {
    setOnClickListener { action() }
}

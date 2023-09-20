package com.ryanblignaut.featherfinder.ui.component.ext

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

fun DialogFragment.show(context: Context, tag: String? = null) {
    val fragmentActivity: FragmentActivity? = tryGetFragmentActivity(context)
    if (fragmentActivity != null) {
        show(fragmentActivity.supportFragmentManager, tag)
    } else {
        // Handle the case where FragmentActivity is not found
        println("FragmentActivity is not found")
    }
}

fun tryGetFragmentActivity(context: Context): FragmentActivity? {
    var fragmentActivity: FragmentActivity? = null
    var tempContext = context
    // Context seems to be androidx.appcompat.view.ContextThemeWrapper but that extends ContextWrapper so this should be safe.
    while (tempContext is ContextWrapper) {
        if (tempContext is FragmentActivity) {
            fragmentActivity = tempContext
            break
        }
        tempContext = tempContext.baseContext
    }
    return fragmentActivity
}
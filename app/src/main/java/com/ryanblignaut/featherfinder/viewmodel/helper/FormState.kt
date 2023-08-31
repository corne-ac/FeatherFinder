package com.ryanblignaut.featherfinder.viewmodel.helper

import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FormState(
    private val valueField: TextInputEditText,
    private val errorField: TextInputLayout,
    private val key: String,
    val map: IFormState,
    val predicate: (String?) -> Int?,
) {
    var isValid: Boolean = false
    fun validate() {
        val data = map.getData(key)
        // This is just so that the login button is disabled if the user has not entered anything.
        if (data == null) {
            isValid = false
            return
        }
        // If the predicate returns a value, then the data is invalid, the value will be the error message.
        predicate(data)?.let {
            errorField.error = it.let { err -> valueField.context.getString(err) }
            isValid = false
        } ?: run {
            errorField.error = null
            isValid = true
        }
    }

    fun attachListener() {
        valueField.doAfterTextChanged {
            map.dataChanged(key, it.toString())
        }
    }
}

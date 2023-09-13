package com.ryanblignaut.featherfinder.viewmodel.helper

import com.ryanblignaut.featherfinder.ui.component.TextInput

class FormStateNew(
    input: TextInput,
    key: String,
    map: IFormState,
    predicate: (String?) -> Int?,
) : FormState(input.binding.value, input.binding.valueInputLayout, key, map, predicate)

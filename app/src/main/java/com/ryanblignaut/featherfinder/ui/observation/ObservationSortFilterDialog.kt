package com.ryanblignaut.featherfinder.ui.observation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.ui.component.DatePickerInput
import com.ryanblignaut.featherfinder.viewmodel.observation.AllObservationsViewModel

class ObservationSortFilterDialog(var a: (filterTime: String, nameSort: Boolean) -> Unit): DialogFragment() {
    private val model: AllObservationsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_dialog_observations_sort_filter, container, false)
        var switchSortName = rootView.findViewById<SwitchCompat>(R.id.switchSortName)
//        var switchSortDate = rootView.findViewById<SwitchCompat>(R.id.switchSortDate)
        rootView.findViewById<Button>(R.id.sortFilterCancel).setOnClickListener {
            dismiss()
        }

        rootView.findViewById<Button>(R.id.sortFilterConfirm).setOnClickListener {

            var filterTime = ""
            filterTime = rootView.findViewById<DatePickerInput>(R.id.filterDate).getText()


            var isNameSort: Boolean = switchSortName.isChecked

            a(filterTime, isNameSort)
            dismiss()
        }

        return rootView
    }
}
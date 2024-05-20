package com.example.reminderapp.ui.reminds.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.common.extensions.parentToolbar
import com.example.reminderapp.common.extensions.toggleVisability
import com.example.reminderapp.databinding.FragmentUpdateRemindsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@AndroidEntryPoint
class EditRemindFragment: BaseFragment(R.layout.fragment_update_reminds) {
    private val viewModel: EditRemindViewModel by viewModels<EditRemindViewModelImpl>()
    private val binding by viewBinding(FragmentUpdateRemindsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        bindUi()
    }


    private fun bindUi() {
        parentToolbar {
            isVisible = true
            navigationIcon = R.drawable.ic_back
            navigationIconClick = {
                popBack()
            }
            title = getString(R.string.update_remind_title)
        }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.checkBoxChanged(isChecked)
        }
        binding.setTimeBtn.setOnClickListener {
            openTimePickerDialog()
        }
        binding.setDateBtn.setOnClickListener {
            openDatePickedDialog()
        }
        binding.setAlarmBtn.setOnClickListener {
            viewModel.updateRemindClicked()
        }
        binding.title.addTextChangedListener {
            viewModel.titleChanged(it.toString())
        }
        binding.description.addTextChangedListener {
            viewModel.descriptionChanged(it.toString())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        checkBoxState.listenValue{
            binding.addAlarmGroup.toggleVisability(it)
            binding.checkbox.isChecked = it
        }
        uiTimeData.listenValue(binding.tvTime::setText)
        uiDateData.listenValue(binding.tvDate::setText)
        titleDataFirst.listenValue(binding.title::setText)
        descriptionDataFirst.listenValue(binding.description::setText)
        navigationFlow.listenValue(::onNavigate)
        loadingState.listenValue(binding.progressLayout::toggleVisability)
    }

    private fun openTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hour1: Int, minute1: Int ->
                viewModel.remindTimeChanged(hour1, minute1)
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun openDatePickedDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year1: Int, month1: Int, day1: Int ->
                val monthBuff = month1 + 1
                val dateString = StringBuilder()
                if(day1 < 10){
                    dateString.append("0")
                }
                dateString.append("$day1.")
                if(monthBuff < 10){
                    dateString.append("0")
                }
                dateString.append("$monthBuff.")
                dateString.append("$year1")
                viewModel.remindDateChanged(dateString.toString())
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun onNavigate(navId: Int?) {
        navId?.let {
            popBack()
        }
    }

    @Parcelize
    data class Param(
        val id: Int
    ):Parcelable
}
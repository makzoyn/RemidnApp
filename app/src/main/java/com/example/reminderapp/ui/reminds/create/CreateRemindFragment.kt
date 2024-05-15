package com.example.reminderapp.ui.reminds.create

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.common.extensions.parentToolbar
import com.example.reminderapp.common.extensions.toggleVisability
import com.example.reminderapp.databinding.FragmentCreateRemindsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class CreateRemindFragment: BaseFragment(R.layout.fragment_create_reminds) {
    private val viewModel: CreateRemindViewModel by viewModels<CreateRemindViewModelImpl>()
    private val binding by viewBinding(FragmentCreateRemindsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        bindUi()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            requireActivity().activityResultRegistry,
            ::callbackNotificationsPermission
        )

    }

    private var shouldShowRationale = false
    private val permissionRequestRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    private fun bindUi() {
        parentToolbar {
            isVisible = true
            navigationIcon = R.drawable.ic_back
            navigationIconClick = {
                popBack()
            }
            title = getString(R.string.remind_create_title)
        }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.checkBoxChanged(isChecked)
        }
        binding.tvTime.text = "Выберите время"
        binding.tvDate.text = "Выберите дату"
        binding.setTimeBtn.setOnClickListener {
            openTimePickerDialog()
        }
        binding.setDateBtn.setOnClickListener {
            openDatePickedDialog()
        }
        binding.setAlarmBtn.setOnClickListener {
            if (permissionRequestRequired) {
                checkPermission()
            }
            viewModel.createRemindClicked()
        }
        binding.title.addTextChangedListener {
            viewModel.titleChanged(it.toString())
        }
        binding.description.addTextChangedListener {
            viewModel.descriptionChanged(it.toString())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        checkBoxState.listenValue(binding.addAlarmGroup::toggleVisability)
        uiTimeData.listenValue(binding.tvTime::setText)
        uiDateData.listenValue(binding.tvDate::setText)
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
            navigate(navId)
        }
    }

    private fun callbackNotificationsPermission(isGranted: Boolean) {
        if (!isGranted) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission() {
        shouldShowRationale = shouldShowRequestPermissionRationale(notificationPermission)
        notificationPermissionLauncher.launch(notificationPermission)
    }
}
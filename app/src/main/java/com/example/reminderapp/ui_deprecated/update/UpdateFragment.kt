package com.example.reminderapp.ui_deprecated.update

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.R
import com.example.reminderapp.databinding.FragmentUpdateBinding
import com.example.reminderapp.utils.FormatTime
import com.example.reminderapp.viewmodel.RemindViewModel
import java.util.Calendar


class UpdateFragment : Fragment() {
    private val viewModel: RemindViewModel by viewModels()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentUpdateBinding.inflate(inflater)

//        val args = UpdateFragmentArgs.fromBundle(requireArguments())

        binding.apply {
//            updateEtTitle.setText(args.remindEntry.title)
//            updateEtDescription.setText(args.remindEntry.description)
//            updateTvTime.text = args.remindEntry.time
//            updateTvDate.text = args.remindEntry.date


            updateTimeBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { /*timePicker: TimePicker?*/_, hour1: Int, minute1: Int ->
                        updateTvTime.text = FormatTime.formatTime(hour1, minute1)
                    },
                    hour,
                    minute,
                    false
                )
                timePickerDialog.show()
            }
            updateDateBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { /*datePicker: DatePicker?*/_, year1: Int, month1: Int, day1: Int ->
                        val monthBuff = month1 + 1
//                        tvDate.text = "$day1.$monthBuff.$year1"
                        val dateString = StringBuilder()
                        dateString.append("$year1.")
                        if(monthBuff < 10){
                            dateString.append("0")
                        }
                        dateString.append("$monthBuff.")
                        if(day1 < 10){
                            dateString.append("0")
                        }
                        dateString.append("$day1")
                        updateTvDate.text = dateString.toString()
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }
            updateAlarmBtn.setOnClickListener {
                if (TextUtils.isEmpty(updateEtTitle.text)) {
                    Toast.makeText(requireContext(), "Title empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(updateEtDescription.text)) {
                    Toast.makeText(requireContext(), "Description empty!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(updateTvTime.text)) {
                    Toast.makeText(requireContext(), "Set time!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(updateTvDate.text)) {
                    Toast.makeText(requireContext(), "Set date!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val title = updateEtTitle.text.toString()
                val description = updateEtDescription.text.toString()
                val time = updateTvTime.text.toString()
                val date = updateTvDate.text.toString()

//                val remindEntry = RemindEntry(
//                    args.remindEntry.id,
//                    title,
//                    description,
//                    time,
//                    date,
//                    args.remindEntry.alarmID
//                )
//                viewModel.update(remindEntry)
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}
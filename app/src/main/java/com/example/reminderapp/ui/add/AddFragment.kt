package com.example.reminderapp.ui.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.R
import com.example.reminderapp.database.RemindEntry
import com.example.reminderapp.databinding.FragmentAddBinding
import com.example.reminderapp.viewmodel.RemindViewModel
import java.util.Calendar


class AddFragment : Fragment() {

    private val viewModel: RemindViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddBinding.inflate(inflater)
        

        binding.apply {

            setTimeBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    OnTimeSetListener { /*timePicker: TimePicker?*/_, hour1: Int, minute1: Int ->
                        tvTime.text = formatTime(hour1, minute1)
                    },
                    hour,
                    minute,
                    false
                )
                timePickerDialog.show()
            }
            setDateBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    OnDateSetListener { /*datePicker: DatePicker?*/_, year1: Int, month1: Int, day1: Int ->
                        val monthBuff = month1 + 1
                        tvDate.text = "$day1-$monthBuff-$year1"
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }
            setAlarmBtn.setOnClickListener {
                if (TextUtils.isEmpty(etTitle.text)) {
                    Toast.makeText(requireContext(), "Title empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(etDescription.text)) {
                    Toast.makeText(requireContext(), "Description empty!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(tvTime.text)) {
                    Toast.makeText(requireContext(), "Set time!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(tvDate.text)) {
                    Toast.makeText(requireContext(), "Set date!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                val title = etTitle.text.toString()
                val description = etDescription.text.toString()
                val time = tvTime.text.toString()
                val date = tvDate.text.toString()
                val remindEntry = RemindEntry(
                    0,
                    title,
                    description,
                    time,
                    date
                )

                viewModel.insert(remindEntry)
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_remindFragment)
            }
        }
        return binding.root
    }


    private fun formatTime(hour: Int, minute: Int): String {
        val formattedMinute = if (minute / 10 == 0) {
            "0$minute"
        } else {
            "" + minute
        }
        val time = if (hour == 0) {
            "12:$formattedMinute AM"
        } else if (hour < 12) {
            "$hour:$formattedMinute AM"
        } else if (hour == 12) {
            "12:$formattedMinute PM"
        } else {
            val temp = hour - 12
            "$temp:$formattedMinute PM"
        }
        return time
    }
}
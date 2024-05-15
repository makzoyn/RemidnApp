package com.example.reminderapp.ui_deprecated.add

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.R
import com.example.reminderapp.alarm.AlarmBroadcast
import com.example.reminderapp.databinding.FragmentAddBinding
import com.example.reminderapp.utils.FormatTime
import com.example.reminderapp.viewmodel.RemindViewModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Random


class AddFragment : Fragment() {

    private val viewModel: RemindViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddBinding.inflate(inflater)
        lateinit var timeToNotify: String
        binding.apply {
            setTimeBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { /*timePicker: TimePicker?*/_, hour1: Int, minute1: Int ->
                        timeToNotify = "$hour1:$minute1"
                        tvTime.text = FormatTime.formatTime(hour1, minute1)
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
                        tvDate.text = dateString.toString()
                        //tvDate.text = "$day1-${month1 + 1}-$year"
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
                val random = Random(System.currentTimeMillis())
                val alarmID = random.nextInt()
//                val remindEntry = RemindEntry(
//                    0,
//                    title,
//                    description,
//                    time,
//                    date,
//                    alarmID
//                )

//                viewModel.insert(remindEntry)
                setAlarm(title, date, timeToNotify, description, alarmID)

            }
        }
        return binding.root

    }

    @SuppressLint("SimpleDateFormat")
    private fun setAlarm(text: String, date: String, time:String, description: String, alarmID : Int) {
        val am : AlarmManager = getSystemService(requireContext(), AlarmManager::class.java) as AlarmManager
        val intent = Intent(requireContext(), AlarmBroadcast::class.java)
        intent.putExtra("event", text)
        intent.putExtra("time", time)
        intent.putExtra("date", date)
        intent.putExtra("description", description)

        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            alarmID,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )
        val dateAndTime = "$date $time"
        val formatter : DateFormat = SimpleDateFormat("yyyy.M.d hh:mm")
        try {
            val date1 : Date = formatter.parse(dateAndTime) as Date
            am.set(AlarmManager.RTC_WAKEUP, date1.time, pendingIntent)
        }catch (e : ParseException) {
            e.printStackTrace()
        }
        Toast.makeText(requireContext(), "$alarmID", Toast.LENGTH_SHORT).show()

    }


}
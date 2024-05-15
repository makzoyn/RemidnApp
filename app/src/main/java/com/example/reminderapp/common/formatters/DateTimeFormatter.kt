package com.example.reminderapp.common.formatters

import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

interface DateTimeFormatter {
    fun formatDateToUi(date: String): String
    fun formatDateFromUi(date: String): String
    fun formatTimeToUi(time: String): String
    fun formatTimeFromUi(time: String): String
    fun formatTime(hour: Int, minute: Int): String
}

class DateTimeFormatterImpl @Inject constructor(

) : DateTimeFormatter{
    override fun formatDateToUi(date: String): String {
        val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
        val dateTime = DateTime.parse(date, formatter)
        val dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy")
        val formattedDate = dateFormatter.print(dateTime)
        return formattedDate.toString()
    }
    override fun formatTime(hour: Int, minute: Int): String {
        val localTime = LocalTime(hour, minute)
        val amPm = if (hour < 12) "AM" else "PM"
        return localTime.toString("h:mm") + " " + amPm
    }

    override fun formatDateFromUi(date: String): String {
        TODO("Not yet implemented")
    }

    override fun formatTimeToUi(time: String): String {
        val formatter = DateTimeFormat.forPattern("HH:mm:ss")
        val dateTime = DateTime.parse(time, formatter)
        val timeFormatter = DateTimeFormat.forPattern("hh:mm a")
        val formattedTime = timeFormatter.print(dateTime)
        return formattedTime.toString()
    }

    override fun formatTimeFromUi(time: String): String {
        TODO("Not yet implemented")
    }

}
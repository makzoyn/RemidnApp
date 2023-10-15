package com.example.reminderapp.utils



class FormatTime {
    companion object {
        fun formatTime(hour: Int, minute: Int): String {
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
}
package com.example.reminderapp.domain.model

import com.example.reminderapp.common.formatters.DateTimeFormatter
import com.example.reminderapp.ui.reminds.adapter.model.RemindItem

data class RemindsModel(
    val reminds: List<RemindModel>
)

fun List<RemindModel>.mapToItems(
    dateTimeFormatter: DateTimeFormatter
) = map {
    RemindItem(
        id = it.id,
        title = it.title,
        description = it.description,
        time = it.time?.let { time -> dateTimeFormatter.formatTimeToUi(time) },
        date = it.date?.let { date -> dateTimeFormatter.formatDateToUi(date) },
        isNotified = it.isNotified,
        needToNotified = it.needToNotified
    )
}

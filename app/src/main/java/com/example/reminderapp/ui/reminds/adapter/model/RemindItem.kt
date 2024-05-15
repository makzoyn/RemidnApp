package com.example.reminderapp.ui.reminds.adapter.model

import ru.ideast.srochnodengi.ui.model.DiffItem

data class RemindItem(
    val id: Int,
    val title: String,
    val description: String?,
    val time: String?,
    val date: String?,
    val isNotified: Boolean,
    val isSelected: Boolean = false
) : DiffItem {
    override fun areItemsTheSame(newItem: DiffItem): Boolean =
        newItem is RemindItem && this.id == newItem.id

    override fun areContentsTheSame(newItem: DiffItem): Boolean = newItem is RemindItem &&
            this == newItem

}

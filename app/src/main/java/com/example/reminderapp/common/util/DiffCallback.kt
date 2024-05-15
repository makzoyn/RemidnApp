package ru.ideast.srochnodengi.ui.model

import androidx.recyclerview.widget.DiffUtil

class DiffCallback : DiffUtil.ItemCallback<DiffItem>() {
    override fun getChangePayload(oldItem: DiffItem, newItem: DiffItem): Any? =
        oldItem.getChangePayload(newItem)

    override fun areItemsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean =
        oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean =
        oldItem.areContentsTheSame(newItem)
}
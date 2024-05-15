package ru.ideast.srochnodengi.ui.model

interface DiffItem {
    fun areItemsTheSame(newItem: DiffItem): Boolean
    fun areContentsTheSame(newItem: DiffItem): Boolean
    fun getChangePayload(newItem: DiffItem): Any? = null
}
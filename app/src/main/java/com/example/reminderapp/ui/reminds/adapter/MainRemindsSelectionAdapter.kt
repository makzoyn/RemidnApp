package com.example.reminderapp.ui.reminds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ItemAlarmRemindBinding
import com.example.reminderapp.databinding.ItemSelectionRemindBinding
import com.example.reminderapp.ui.reminds.adapter.model.RemindItem
import com.example.reminderapp.ui.reminds.adapter.viewholders.RemindAlarmViewHolder
import com.example.reminderapp.ui.reminds.adapter.viewholders.RemindSelectionViewHolder
import ru.ideast.srochnodengi.ui.model.DiffCallback
import ru.ideast.srochnodengi.ui.model.DiffItem

class MainRemindsSelectionAdapter (
    private val remindClick: (id: Int) -> Unit,
    private val longClick: (id: Int) -> Unit
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val differ: AsyncListDiffer<DiffItem> = AsyncListDiffer(this, DiffCallback())

    override fun getItemViewType(position: Int): Int =
        when (currentList()[position]) {
            is RemindItem -> R.layout.item_selection_remind

            else -> throw IllegalArgumentException("unknown item ${currentList()[position].javaClass.simpleName}")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_selection_remind -> RemindSelectionViewHolder(
                ItemSelectionRemindBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                remindClick,
                longClick
            )


            else -> throw IllegalArgumentException("unknown viewType $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_selection_remind -> (holder as RemindSelectionViewHolder).bind(currentList()[position] as RemindItem)
        }
    }

    override fun getItemCount(): Int = currentList().size

    fun submitList(list: List<DiffItem>) {
        differ.submitList(list)
    }

    private fun currentList(): List<DiffItem> =
        differ.currentList


}

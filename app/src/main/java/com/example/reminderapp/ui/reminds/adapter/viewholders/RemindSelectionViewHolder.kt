package com.example.reminderapp.ui.reminds.adapter.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.toggleVisability
import com.example.reminderapp.databinding.ItemSelectionRemindBinding
import com.example.reminderapp.ui.reminds.adapter.model.RemindItem

class RemindSelectionViewHolder(
    private val binding: ItemSelectionRemindBinding,
    private val remindClick: (id: Int) -> Unit,
    private val longClick: (id: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RemindItem) {
        binding.title.text = item.title
        binding.description.toggleVisability(item.description != null)
        binding.date.toggleVisability(item.date != null)
        binding.time.toggleVisability(item.time != null)
        binding.description.text = item.description
        binding.date.text = item.date
        binding.time.text = item.time
        binding.root.setOnLongClickListener {
            longClick(item.id)
            true
        }
        binding.selection.toggleVisability(item.isSelected)
        when{
            item.date == null && item.time == null -> {
                binding.root.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.onlyText)
            }
            item.isNotified -> {
                binding.root.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.notifiedCardColor)
            }
            else -> {
                binding.root.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.white)
            }
        }

        binding.root.setOnClickListener {
            remindClick(item.id)
        }

    }
}
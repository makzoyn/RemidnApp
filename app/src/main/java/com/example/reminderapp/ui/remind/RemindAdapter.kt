package com.example.reminderapp.ui.remind

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.database.RemindEntry
import com.example.reminderapp.databinding.LayoutRowBinding

class RemindAdapter : ListAdapter<RemindEntry, RemindAdapter.ViewHolder>(RemindDiffCallback) {
    companion object RemindDiffCallback : DiffUtil.ItemCallback<RemindEntry>(){
        override fun areItemsTheSame(oldItem: RemindEntry, newItem: RemindEntry) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RemindEntry, newItem: RemindEntry) =
            oldItem == newItem
    }
    class ViewHolder(val binding: LayoutRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(remindEntry: RemindEntry) {
            binding.remindEntry = remindEntry
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}
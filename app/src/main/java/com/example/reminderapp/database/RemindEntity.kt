package com.example.reminderapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.reminderapp.domain.model.RemindModel

@Entity(tableName = "reminds")
data class RemindEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String?,
    val time: String?,
    val date: String?,
    val isNotified: Boolean,
    val needToNotified: Boolean
)

fun List<RemindEntity>.mapToDomainList() = map {
    it.mapToDomain()
}

fun RemindEntity.mapToDomain() = RemindModel(
    id = id,
    title = title,
    description = description,
    time = time,
    date = date,
    isNotified = isNotified,
    needToNotified = needToNotified
)
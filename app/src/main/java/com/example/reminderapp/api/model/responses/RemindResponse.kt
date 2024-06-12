package com.example.reminderapp.api.model.responses

import com.example.reminderapp.database.RemindEntity
import com.example.reminderapp.domain.model.RemindModel
import com.google.gson.annotations.SerializedName

data class RemindResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("notified") val isNotified: Boolean,
    @SerializedName("need_to_notified") val needToNotified: Boolean
)

fun RemindResponse.mapToDomain() = RemindModel(
    id = id,
    title = title,
    description = description,
    time = time,
    date = date,
    isNotified = isNotified,
    needToNotified = needToNotified
)

fun RemindResponse.mapToDao() = RemindEntity(
    id = id,
    title = title,
    description = description,
    time = time,
    date = date,
    isNotified = isNotified,
    needToNotified = needToNotified
)

fun List<RemindResponse>.toDomainList() = map { it.mapToDomain() }

fun List<RemindResponse>.toDaoList() = map { it.mapToDao() }


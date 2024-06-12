package com.example.reminderapp.api.model.responses

import com.example.reminderapp.database.RemindEntity
import com.example.reminderapp.domain.model.RemindsModel
import com.google.gson.annotations.SerializedName

data class RemindsResponse(
    @SerializedName("reminds") val reminds: List<RemindResponse>
)

fun RemindsResponse.mapToDomain() = RemindsModel(
    reminds = reminds.toDomainList()
)

fun RemindsResponse.mapToDao() = reminds.toDaoList()


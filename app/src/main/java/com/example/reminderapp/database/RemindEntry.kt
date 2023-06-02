package com.example.reminderapp.database


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "reminds_table")
data class RemindEntry (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String,
    var time: String,
    var date: String,
    var alarmID : Int
) : Parcelable
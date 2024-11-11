package com.example.todolistapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "deadlineDate")
    var deadlineDate: String?,

    @ColumnInfo(name = "category")
    var categoryName: String = "Default",

    @ColumnInfo(name = "status")
    var isFinished: Boolean = false
)
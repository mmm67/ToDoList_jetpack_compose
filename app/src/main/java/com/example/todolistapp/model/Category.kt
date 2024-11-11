package com.example.todolistapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "categories")
data class Category (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "taskCounts")
    var taskCounts: Int = 0,

    @ColumnInfo(name = "finishedTasksCount")
    var finishedTasksCount: Int = 0,

    @ColumnInfo(name = "expiredTasksCount")
    var expiredTasksCount: Int = 0,
)
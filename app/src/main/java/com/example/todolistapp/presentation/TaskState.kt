package com.example.todolistapp.presentation

import com.example.todolistapp.model.Task

data class TaskState(
    val allTasks: List<Task> = emptyList(),
    val expiredTasks: List<Task> = emptyList(),
    val finishedTasks: List<Task> = emptyList(),
    val description: String = "",
    val deadlineDate: String? = null,
    val isFinished: Boolean = false,
    val id: Int = 0,
    val category: String = "Default",
    val descriptionEmptyError: Boolean = false
)
package com.example.todolistapp.presentation

import com.example.todolistapp.model.Category

data class CategoryState(
    val selectedFilterCategory: String = "",
    val taskCounts: Int = 0,
    val finishedTasksCount: Int = 0,
    val expiredTasksCount: Int = 0,
    val categories: List<Category> = emptyList()
)
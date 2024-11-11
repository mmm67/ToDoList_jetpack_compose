package com.example.todolistapp.presentation

import com.example.todolistapp.model.Category
import com.example.todolistapp.model.Task

sealed interface ToDoListEvent {
    // Task event
    data object UpsertTask : ToDoListEvent
    data class DeleteTask(val task: Task) : ToDoListEvent
    data class SetDescription(val description: String) : ToDoListEvent
    data class SetDeadline(val deadline: String) : ToDoListEvent
    data class FindTasksBy(val category: String) : ToDoListEvent
    data class SetFinish(val isFinished: Boolean) : ToDoListEvent
    data class SetTaskCategory(val category: String) : ToDoListEvent
    data class FinishTask(val task: Task) : ToDoListEvent
    data class AddNewCategory(val newCategory: String) : ToDoListEvent
    data class SetFilterCategory(val filterCategory: String) : ToDoListEvent
    data class DeleteCategory(val category: Category) : ToDoListEvent
    data class DeleteCategoryExpiredTasks(val category: Category) : ToDoListEvent
    data class DeleteCategoryFinishedTasks(val category: Category) : ToDoListEvent
    data class FinishCategoryTasks(val category: Category) : ToDoListEvent
}


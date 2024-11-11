package com.example.todolistapp.presentation.screens

sealed class ScreenNames(val route: String) {
    data object Home: ScreenNames("Home")
    data object TaskForm : ScreenNames("TaskForm")
    data object Categories : ScreenNames("Categories")
    data object DoneTasks : ScreenNames("DoneTasks")
    data object ExpiredTasks : ScreenNames("ExpiredTasks")
}
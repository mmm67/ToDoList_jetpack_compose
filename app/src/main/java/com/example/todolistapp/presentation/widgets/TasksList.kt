package com.example.todolistapp.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolistapp.presentation.TaskState
import com.example.todolistapp.presentation.ToDoListEvent
import com.example.todolistapp.presentation.screens.ScreenNames

@Composable
fun TasksList(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    uiState: TaskState,
    onEvent: (ToDoListEvent) -> Unit,
    navigateTo: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = uiState.allTasks, key = { task -> task.id }) { task ->
            TaskItem(
                task = task,
                onFinishTask = {
                    if (!task.isFinished) {
                        onEvent(ToDoListEvent.FinishTask(task.copy(isFinished = true)))
                    }
                },
                onDeleteTask = { onEvent(ToDoListEvent.DeleteTask(task)) },
                onClick = { navigateTo(ScreenNames.TaskForm.route + "/${task.id}") }
            )
        }
    }
}
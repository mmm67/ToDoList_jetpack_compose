package com.example.todolistapp.presentation.topbars

import NavTopBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.todolistapp.presentation.ToDoListEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    onEvent: (ToDoListEvent) -> Unit,
    taskSaveEvent: SharedFlow<Boolean?>
) {

    LaunchedEffect(Unit) {
        taskSaveEvent.collect { isSuccess ->
            if (isSuccess == true) {
                // Navigate back only if the task was saved successfully
                navController.popBackStack()
            }
        }
    }

    NavTopBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = "Task Details")
        },
        actions = {
            IconButton(onClick = {
                onEvent(ToDoListEvent.UpsertTask)
            }) {
                Icon(Icons.Filled.Check, contentDescription = "Menu")
            }
        },
        canNavigateBack = true,
        scrollBehavior = scrollBehavior,
        navigateUp = {
            navController.popBackStack()
        }
    )
}
package com.example.todolistapp.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.todolistapp.R
import com.example.todolistapp.model.MenuItem
import com.example.todolistapp.presentation.ToDoListEvent
import com.example.todolistapp.presentation.TaskViewModel
import com.example.todolistapp.presentation.topbars.HomeTopBar
import com.example.todolistapp.dialogs.NewCategoryDialog
import com.example.todolistapp.presentation.widgets.TasksList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: TaskViewModel
) {
    val uiState by viewModel.taskUiState.collectAsState()
    val categoryState by viewModel.categoryUiState.collectAsState()

    var showAddNewCategoryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ToDoListEvent.FindTasksBy(categoryState.selectedFilterCategory))
    }

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    navController.navigate(ScreenNames.TaskForm.route)
                },
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, "Large floating action button")
            }
        },
        topBar = {
            HomeTopBar(scrollBehavior = scrollBehavior,
                categoryState = categoryState,
                onCategoryChanged = {
                    viewModel.onEvent(ToDoListEvent.SetFilterCategory(it))
                }, onFilterTasksByCategory = {
                    viewModel.onEvent(ToDoListEvent.FindTasksBy(it))
                }, onAddNewCategory = {
                    showAddNewCategoryDialog = it
                }, listOf(MenuItem(iconId = R.drawable.category, title = "All categories", onclick = {
                    navController.navigate(ScreenNames.Categories.route)
                }), MenuItem(iconId = R.drawable.done, title = "Finished Tasks", onclick = {
                    navController.navigate(ScreenNames.DoneTasks.route)
                }), MenuItem(iconId = R.drawable.overdue, title = "Expired Tasks", onclick = {
                        navController.navigate(ScreenNames.ExpiredTasks.route)
                    }))
            )

        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        TasksList(
            innerPadding = innerPadding,
            uiState = uiState,
            onEvent = { viewModel.onEvent(it) }) {
            navController.navigate(it)
        }

        if (showAddNewCategoryDialog) {
            NewCategoryDialog(onDismiss = {
                showAddNewCategoryDialog = false
            }) { newCategoryName ->
                showAddNewCategoryDialog = false
                viewModel.onEvent(ToDoListEvent.AddNewCategory(newCategoryName))
            }
        }
    }
}

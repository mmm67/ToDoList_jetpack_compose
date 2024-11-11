package com.example.todolistapp.presentation.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.todolistapp.R
import com.example.todolistapp.model.TaskType
import com.example.todolistapp.presentation.ToDoListEvent
import com.example.todolistapp.presentation.TaskViewModel
import com.example.todolistapp.model.MenuItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryOverviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: TaskViewModel
) {
    val categoryState by viewModel.categoryUiState.collectAsState()

    CategoryTaskScreen(
        modifier = modifier,
        navController = navController,
        scrollBehavior = scrollBehavior,
        categoryState = categoryState,
        title = "All Categories",
        menuItems = listOf(
            MenuItem(
                iconId = R.drawable.category,
                title = "All categories",
                onclick = {
                    navController.navigate(ScreenNames.Categories.route)
                }
            ),
            MenuItem(
                iconId = R.drawable.done,
                title = "Finished Tasks",
                onclick = {
                    navController.navigate(ScreenNames.DoneTasks.route)
                }
            ),
            MenuItem(
                iconId = R.drawable.overdue,
                title = "Expired Tasks",
                onclick = {
                    navController.navigate(ScreenNames.ExpiredTasks.route)
                }
            )
        ),
        taskType = TaskType.All,
        onMarkItemDone = {
            viewModel.onEvent(ToDoListEvent.FinishCategoryTasks(it))
        },
        onDeleteCategory = {
            viewModel.onEvent(ToDoListEvent.DeleteCategory(it))
        },
        onEvent = viewModel::onEvent
    )
}


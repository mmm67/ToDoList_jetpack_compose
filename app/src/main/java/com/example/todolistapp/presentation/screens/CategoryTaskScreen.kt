package com.example.todolistapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolistapp.dialogs.DeleteCategoryTasksConfirmationDialog
import com.example.todolistapp.model.Category
import com.example.todolistapp.model.MenuItem
import com.example.todolistapp.model.TaskType
import com.example.todolistapp.presentation.CategoryState
import com.example.todolistapp.presentation.ToDoListEvent
import com.example.todolistapp.presentation.topbars.CategoryTaskOverviewTopBar
import com.example.todolistapp.presentation.widgets.CategoryTaskOverviewItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    categoryState: CategoryState,
    title: String,
    menuItems: List<MenuItem>,
    taskType: TaskType,
    onMarkItemDone: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onEvent: (ToDoListEvent) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            CategoryTaskOverviewTopBar(
                scrollBehavior = scrollBehavior,
                title = title,
                menuItems = menuItems
            ) {
                navController.popBackStack()
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = categoryState.categories, key = { category -> category.id }) { category ->
                CategoryTaskOverviewItem(
                    category = category,
                    onMarkItemDone = {
                        onMarkItemDone(category)
                    },
                    onDeleteItem = {
                        categoryToDelete = category
                        showDeleteDialog = true
                    },
                    onItemClick = {
                        onEvent(ToDoListEvent.SetFilterCategory(category.name))
                        navController.navigate(ScreenNames.Home.route)
                    },
                    taskType = taskType
                )
            }
        }
    }

    if (showDeleteDialog && categoryToDelete != null) {
        DeleteCategoryTasksConfirmationDialog(
            category = categoryToDelete?.name ?: "",
            onDismiss = { showDeleteDialog = false }) {
            categoryToDelete?.let {
                onDeleteCategory(it)
            }
        }
    }
}

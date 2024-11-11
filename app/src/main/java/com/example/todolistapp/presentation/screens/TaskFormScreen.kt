package com.example.todolistapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolistapp.R
import com.example.todolistapp.presentation.widgets.ToDoListDatePicker
import com.example.todolistapp.presentation.CategoryState
import com.example.todolistapp.presentation.ToDoListEvent
import com.example.todolistapp.presentation.TaskState
import com.example.todolistapp.presentation.TaskViewModel
import com.example.todolistapp.presentation.topbars.AddTaskTopBar
import com.example.todolistapp.dialogs.NewCategoryDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    taskId: Int?
) {

    val taskState by viewModel.taskUiState.collectAsState()
    val categoryState by viewModel.categoryUiState.collectAsState()

    LaunchedEffect(taskId) {
        if (taskId != null) {
            // Load the task if taskId is not null (editing an existing task)
            viewModel.loadTask(taskId)
        } else {
            // Reset the task state to default if taskId is null (creating a new task)
            viewModel.resetTaskState()
            viewModel.onEvent(ToDoListEvent.SetTaskCategory(categoryState.selectedFilterCategory))
        }
    }

    Scaffold(
        topBar = {
            AddTaskTopBar(
                navController,
                scrollBehavior = scrollBehavior,
                onEvent = viewModel::onEvent,
                viewModel.taskSaveResult
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        MainContent(
            modifier = modifier.padding(innerPadding),
            taskState,
            categoryState,
            onEvent = viewModel::onEvent,
        )
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    taskState: TaskState,
    categoryState: CategoryState,
    onEvent: (ToDoListEvent) -> Unit
) {
    var description = taskState.description
    var isFinished = taskState.isFinished
    var selectedDate = taskState.deadlineDate ?: ""
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Describe your task", style = MaterialTheme.typography.bodyMedium)

        // Description TextField
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                onEvent(ToDoListEvent.SetDescription(description))
            },
            label = { Text("Enter new task") },
            modifier = Modifier.fillMaxWidth(),
            isError = taskState.descriptionEmptyError
        )

        // Checkbox for "Finish Task"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Checkbox(
                checked = isFinished,
                onCheckedChange = {
                    isFinished = it
                    onEvent(ToDoListEvent.SetFinish(it))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Finish Task")
        }

        // Date Picker
        ToDoListDatePicker(selectedDate, onSelectedDate = { date ->
            selectedDate = date
            onEvent(ToDoListEvent.SetDeadline(date))
        })


        // Category Dropdown
        CategoryDropDownMenu(
            modifier,
            categoryState = categoryState,
            taskState = taskState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDownMenu(
    modifier: Modifier = Modifier,
    taskState: TaskState,
    categoryState: CategoryState,
    onEvent: (ToDoListEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = categoryState.categories
    var selectedCategory = taskState.category
    var showNewCategoryDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(text = item.name)
                            },
                            onClick = {
                                selectedCategory = item.name
                                onEvent(ToDoListEvent.SetTaskCategory(selectedCategory))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Add Category Button
        IconButton(
            modifier = Modifier.scale(2f).padding(10.dp),
            onClick = { showNewCategoryDialog = true }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.add_category_icon),
                contentDescription = "Add new category"
            )
        }

        if (showNewCategoryDialog) {
            NewCategoryDialog(onDismiss = {
                showNewCategoryDialog = false
            }) { newCategoryName ->
                selectedCategory = newCategoryName
                showNewCategoryDialog = false
                onEvent(ToDoListEvent.AddNewCategory(newCategoryName))
            }
        }
    }
}

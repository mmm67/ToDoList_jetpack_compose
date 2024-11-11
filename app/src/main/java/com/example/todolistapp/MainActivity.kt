package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolistapp.model.TaskRoomDataBase
import com.example.todolistapp.presentation.TaskViewModel
import com.example.todolistapp.presentation.TaskViewModelFactory
import com.example.todolistapp.presentation.screens.FinishedTasksScreen
import com.example.todolistapp.repositories.TaskRepository
import com.example.todolistapp.presentation.screens.HomeScreen
import com.example.todolistapp.presentation.screens.ScreenNames
import com.example.todolistapp.presentation.screens.CategoryOverviewScreen
import com.example.todolistapp.presentation.screens.ExpiredTasksScreen
import com.example.todolistapp.presentation.screens.TaskFormScreen
import com.example.todolistapp.ui.theme.ToDoListAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListAppTheme {
                ToDoListApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListApp(modifier: Modifier = Modifier) {
    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(
            TaskRepository(
                TaskRoomDataBase.getInstance(LocalContext.current, CoroutineScope(SupervisorJob()))
                    .taskDao()
            )
        )
    )

    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    NavHost(navController, startDestination = ScreenNames.Home.route) {
        composable(ScreenNames.Home.route) {
            HomeScreen(
                modifier = modifier,
                navController = navController,
                scrollBehavior = scrollBehavior,
                viewModel = viewModel
            )
        }

        composable(ScreenNames.Categories.route) {
            CategoryOverviewScreen(
                modifier = modifier,
                navController = navController,
                scrollBehavior = scrollBehavior,
                viewModel = viewModel
            )
        }

        composable(ScreenNames.DoneTasks.route) {
            FinishedTasksScreen(
                modifier = modifier,
                navController = navController,
                scrollBehavior = scrollBehavior,
                viewModel = viewModel
            )
        }

        composable(ScreenNames.ExpiredTasks.route) {
            ExpiredTasksScreen(
                modifier = modifier,
                navController = navController,
                scrollBehavior = scrollBehavior,
                viewModel = viewModel
            )
        }
        composable(ScreenNames.TaskForm.route + "/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            TaskFormScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                scrollBehavior = scrollBehavior,
                taskId = taskId
            )
        }

        composable(ScreenNames.TaskForm.route) {
            TaskFormScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                scrollBehavior = scrollBehavior,
                taskId = null
            )
        }
    }
}


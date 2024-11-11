package com.example.todolistapp.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.model.Category
import com.example.todolistapp.model.TaskType

@Composable
fun CategoryTaskOverviewItem(
    category: Category,
    onMarkItemDone: (Category) -> Unit = {},
    onDeleteItem: (Category) -> Unit = {},
    onItemClick: (Int) -> Unit,
    taskType: TaskType = TaskType.All
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(category.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Category name and task counts
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                when (taskType) {
                    TaskType.Finished ->  {
                        Text(
                            text = " Contains ${category.finishedTasksCount} finished task(s).",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TaskType.All -> {
                        Text(
                            text = " Contains ${category.taskCounts} tasks.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TaskType.Expired -> {
                        Text(
                            text = " Contains ${category.expiredTasksCount} tasks.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            if (taskType == TaskType.All) {
                IconButton(onClick = { onMarkItemDone(category) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.done),
                        contentDescription = "Finish Task",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Delete Task Button
            IconButton(onClick = { onDeleteItem(category) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Tasks",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

package com.example.todolistapp.presentation.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.model.Task
import com.example.todolistapp.utils.isDateExpired

@Composable
fun TaskItem(
    task: Task,
    onFinishTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(task.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Task description and category
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                val finishedTaskTextStyle =
                    MaterialTheme.typography.headlineSmall.copy(textDecoration = TextDecoration.LineThrough)
                val normalTaskTextStyle = MaterialTheme.typography.headlineSmall
                Row (verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = task.description,
                        style = if (task.isFinished) {
                            finishedTaskTextStyle
                        } else {
                            normalTaskTextStyle
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    if (!task.isFinished && isDateExpired(task.deadlineDate)) {
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.overdue),
                            contentDescription = "Overdue",
                        )
                    }
                }


                Text(
                    text = task.categoryName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Finish Task Button
            IconButton(onClick = { onFinishTask(task) }) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Finish Task",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Delete Task Button
            IconButton(onClick = { onDeleteTask(task) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    MaterialTheme {
        TaskItem(
            task = Task(
                description = "Sample Task",
                deadlineDate = "12/31/2023",
                isFinished = false
            ),
            onFinishTask = {},
            onDeleteTask = {},
            onClick = {}
        )
    }
}

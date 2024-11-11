package com.example.todolistapp.dialogs

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun DeleteCategoryTasksConfirmationDialog(
    category: String,
    onDismiss: () -> Unit,
    onDeleteCategory: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete tasks") },
        text = { Text("Are you sure you want to delete all finished tasks related to $category? " +
                "This action cannot be undone.") },
        confirmButton = {
            TextButton(onClick = {
                onDeleteCategory.invoke()
                onDismiss.invoke()
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
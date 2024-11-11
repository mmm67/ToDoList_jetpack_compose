package com.example.todolistapp.dialogs

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun DeleteCategoryConfirmationDialog(
    category: String,
    onDismiss: () -> Unit,
    onDeleteCategory: () -> Unit
) {
    if (category == "Default") {
        Toast.makeText(
            LocalContext.current,
            "Oops! Default category can not be deleted!",
            LENGTH_LONG
        ).show()
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Category") },
        text = { Text("Are you sure you want to delete the '$category' category along with all its tasks? This action cannot be undone..") },
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
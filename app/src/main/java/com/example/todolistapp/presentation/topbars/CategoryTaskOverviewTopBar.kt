package com.example.todolistapp.presentation.topbars

import NavTopBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todolistapp.model.MenuItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTaskOverviewTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    menuItems: List<MenuItem>,
    navigateUp: () -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    NavTopBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = title)
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            DropdownMenu(
                modifier = Modifier.widthIn(200.dp),
                expanded = expanded,
                offset = DpOffset(y = (-20).dp, x = (-15).dp),
                onDismissRequest = { expanded = false }
            ) {
                // Add Task Option
                menuItems.forEach { item ->
                    DropdownMenuItem(
                        leadingIcon = {
                            item.iconId?.let { painterResource(it) }
                                ?.let {
                                    Image(
                                        modifier = Modifier.size(25.dp),
                                        painter = it,
                                        contentDescription = ""
                                    )
                                }
                        },
                        text = { Text(item.title) },
                        onClick = item.onclick
                    )
                }

            }
        },
        canNavigateBack = true,
        scrollBehavior = scrollBehavior,
        navigateUp = navigateUp
    )
}
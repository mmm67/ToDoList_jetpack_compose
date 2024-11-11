package com.example.todolistapp.presentation.topbars

import NavTopBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.model.Category
import com.example.todolistapp.model.MenuItem
import com.example.todolistapp.presentation.CategoryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    categoryState: CategoryState,
    onCategoryChanged: (String) -> Unit,
    onFilterTasksByCategory: (String) -> Unit,
    onAddNewCategory: (Boolean) -> Unit,
    menuItems: List<MenuItem>
) {

    var expanded by remember { mutableStateOf(false) }

    NavTopBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            TaskCategoriesDropDown(
                selectedCategory = categoryState.selectedFilterCategory,
                categories = categoryState.categories,
                onCategoryChanged = onCategoryChanged,
                onFilterTasksByCategory = onFilterTasksByCategory
            ) {
                onAddNewCategory.invoke(it)
            }
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
        canNavigateBack = false,
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun TaskCategoriesDropDown(
    selectedCategory: String,
    categories: List<Category>,
    onCategoryChanged: (String) -> Unit,
    onFilterTasksByCategory: (String) -> Unit,
    onAddNewCategory: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(selectedCategory.ifEmpty {
            "All"
        })
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Show categories")
        }

        DropdownMenu(
            modifier = Modifier.width(200.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.category),
                                contentDescription = "Category",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("All", modifier = Modifier.padding(10.dp))
                        }
                    }
                },
                onClick = {
                    expanded = false
                    onFilterTasksByCategory("All")
                    onCategoryChanged("All")
                }
            )
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.category),
                                    contentDescription = "Category",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    style = MaterialTheme.typography.titleSmall,
                                    text = category.name,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Text(category.taskCounts.toString())
                        }
                    },
                    onClick = {
                        onCategoryChanged(category.name)
                        expanded = false
                        onFilterTasksByCategory(category.name)
                    }
                )
            }

            // "Add New Category" Item
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.add_category_icon),
                            contentDescription = "Add New Category",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Add New Category",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onAddNewCategory.invoke(true)
                }
            )
        }
    }
}

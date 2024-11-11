package com.example.todolistapp.model

import androidx.compose.runtime.Composable

data class MenuItem(val iconId: Int? = null, val title: String, val onclick: () -> Unit)
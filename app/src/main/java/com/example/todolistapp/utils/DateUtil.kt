package com.example.todolistapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertDateToMillis(date: String): Long {
    return try {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val parsedDate = formatter.parse(date)
        parsedDate?.time ?: 0L
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

fun isDateExpired(deadline: String?): Boolean {
    val currentDate = System.currentTimeMillis()
    return deadline?.let { date -> convertDateToMillis(date) < currentDate } == true
}



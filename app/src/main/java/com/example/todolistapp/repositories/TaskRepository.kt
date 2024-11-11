package com.example.todolistapp.repositories

import com.example.todolistapp.model.Category
import com.example.todolistapp.model.Task
import com.example.todolistapp.model.ToDoListDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val toDoListDao: ToDoListDao) {

    suspend fun upsertTask(newTask: Task) {
        toDoListDao.upsertTask(newTask)
    }

    suspend fun insertCategory(category: Category) {
        toDoListDao.insertNewCategory(category)
    }

    fun getAllCategories(): Flow<List<Category>> {
        return toDoListDao.getAllCategories()
    }

    fun getAllTasks(): Flow<List<Task>> {
        return toDoListDao.getAllTasks()
    }

    suspend fun getTaskBy(taskId: Int): Task {
        return toDoListDao.getTaskBy(taskId)
    }

    suspend fun deleteTask(task: Task) {
        toDoListDao.deleteTask(task)
    }

    fun findTasks(category: String): Flow<List<Task>> {
        return toDoListDao.findTasksByCategory(category)
    }

    suspend fun decrementTaskCount(oldCategoryName: String) {
        toDoListDao.decrementTaskCount(oldCategoryName)
    }

    suspend fun incrementTaskCount(categoryName: String) {
        toDoListDao.incrementTaskCount(categoryName)
    }

    suspend fun decrementFinishedTasksCount(oldCategoryName: String) {
        toDoListDao.decrementFinishedTasksCount(oldCategoryName)
    }

    suspend fun incrementFinishedTasksCount(categoryName: String) {
        toDoListDao.incrementFinishedTasksCount(categoryName)
    }

    suspend fun decrementExpiredTasksCount(oldCategoryName: String) {
        toDoListDao.decrementExpiredTasksCount(oldCategoryName)
    }

    suspend fun incrementExpiredTasksCount(categoryName: String) {
        toDoListDao.incrementExpiredTasksCount(categoryName)
    }

    suspend fun deleteCategory(category: Category) {
        toDoListDao.deleteCategoryAlongTasks(category = category)
    }

    suspend fun deleteTaskAndDecrementFinishedTasksCount(task: Task, categoryName: String) {
      toDoListDao.deleteTaskAndDecrementFinishedTasksCount(task, categoryName)
    }

    suspend fun deleteTaskAndDecrementExpiredTasksCount(task: Task, categoryName: String) {
        toDoListDao.deleteTaskAndDecrementExpiredTasksCount(task, categoryName)
    }


    suspend fun finishCategoryTasks(categoryName: String) {
        toDoListDao.finishCategoryTasks(categoryName)
    }

    suspend fun getFinishedTaskCount(categoryName: String): Int {
        return toDoListDao.getFinishedTaskCount(categoryName)
    }

    suspend fun updateFinishedTaskCount(category: String, count: Int) {
        toDoListDao.updateFinishedTaskCount(category, count)
    }
}
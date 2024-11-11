package com.example.todolistapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoListDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Insert
    suspend fun insertNewCategory(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM tasks WHERE category = :category")
    fun findTasksByCategory(category: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskBy(id: Int): Task

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE categories SET taskCounts = taskCounts + 1 WHERE name = :categoryName")
    suspend fun incrementTaskCount(categoryName: String)

    @Query("UPDATE categories SET taskCounts = taskCounts - 1 WHERE name = :categoryName")
    suspend fun decrementTaskCount(categoryName: String)

    @Query("UPDATE categories SET finishedTasksCount = finishedTasksCount + 1 WHERE name = :categoryName")
    suspend fun incrementFinishedTasksCount(categoryName: String)

    @Query("UPDATE categories SET finishedTasksCount = finishedTasksCount - 1 WHERE name = :categoryName")
    suspend fun decrementFinishedTasksCount(categoryName: String)

    @Query("UPDATE categories SET expiredTasksCount = expiredTasksCount + 1 WHERE name = :categoryName")
    suspend fun incrementExpiredTasksCount(categoryName: String)

    @Query("UPDATE categories SET expiredTasksCount = expiredTasksCount - 1 WHERE name = :categoryName")
    suspend fun decrementExpiredTasksCount(categoryName: String)

    @Transaction
    suspend fun deleteTaskAndDecrementFinishedTasksCount(task: Task, categoryName: String) {
        deleteTask(task)
        decrementFinishedTasksCount(categoryName)
        decrementTaskCount(categoryName)
    }

    @Transaction
    suspend fun deleteTaskAndDecrementExpiredTasksCount(task: Task, categoryName: String) {
        deleteTask(task)
        decrementExpiredTasksCount(categoryName)
        decrementTaskCount(categoryName)
    }

    @Transaction
    suspend fun deleteCategoryAlongTasks(category: Category) {
        deleteCategory(category)
        deleteTasksByCategory(category.name)
    }

    @Query("DELETE FROM tasks WHERE category = :categoryName")
    suspend fun deleteTasksByCategory(categoryName: String)

    @Query("UPDATE tasks SET status = 1 WHERE category = :categoryName")
    suspend fun finishCategoryTasks(categoryName: String)

    @Query("SELECT COUNT(*) FROM tasks WHERE category = :categoryName AND status = 1")
    suspend fun getFinishedTaskCount(categoryName: String): Int

    @Query("UPDATE categories SET finishedTasksCount = :count WHERE name = :categoryName")
    suspend fun updateFinishedTaskCount(categoryName: String, count: Int)
}
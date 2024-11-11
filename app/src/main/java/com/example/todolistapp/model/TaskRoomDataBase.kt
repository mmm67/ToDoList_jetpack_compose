package com.example.todolistapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [(Task::class), (Category::class)], version = 1, exportSchema = false)
abstract class TaskRoomDataBase : RoomDatabase() {

    abstract fun taskDao(): ToDoListDao

    companion object {

        @Volatile
        private var INSTANCE: TaskRoomDataBase? = null

        fun getInstance(context: Context, scope: CoroutineScope): TaskRoomDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskRoomDataBase::class.java,
                        "todolist_database"
                    ).fallbackToDestructiveMigration()
                        .addCallback(TaskDatabaseCallback(scope))
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private class TaskDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.taskDao())
                }
            }
        }

        // Function to populate the database with initial data
        suspend fun populateDatabase(dao: ToDoListDao) {
            // Insert initial categories
            dao.insertNewCategory(Category(name = "Default"))
            dao.insertNewCategory(Category(name = "Work"))
            dao.insertNewCategory(Category(name = "Personal"))
            dao.insertNewCategory(Category(name = "Shopping"))
            dao.insertNewCategory(Category(name = "Fitness"))
        }
    }
}


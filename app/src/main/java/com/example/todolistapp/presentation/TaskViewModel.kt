package com.example.todolistapp.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.Category
import com.example.todolistapp.model.Task
import com.example.todolistapp.repositories.TaskRepository
import com.example.todolistapp.utils.isDateExpired
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _taskUiState = MutableStateFlow(TaskState())
    val taskUiState: StateFlow<TaskState> = _taskUiState.asStateFlow()

    private val _categoryUiState = MutableStateFlow(CategoryState())
    val categoryUiState: StateFlow<CategoryState> = _categoryUiState.asStateFlow()

    private val _taskSaveResult = MutableSharedFlow<Boolean?>()
    val taskSaveResult: SharedFlow<Boolean?> = _taskSaveResult.asSharedFlow()

    private var oldTask: Task? = null

    init {
        getAllTasks()
        getAllCategories()
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            taskRepository.getAllCategories().collectLatest { result ->
                _categoryUiState.update {
                    it.copy(categories = result)
                }
            }
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().collectLatest { tasks ->

                val finishedTasks = tasks.filter { it.isFinished }
                val expiredTasks = tasks.filter {
                    isTaskExpired(it)
                }

                _taskUiState.update {
                    it.copy(
                        allTasks = tasks,
                        finishedTasks = finishedTasks,
                        expiredTasks = expiredTasks
                    )
                }
            }
        }
    }

    fun resetTaskState() {
        _taskUiState.value = TaskState()
    }

    private fun isTaskExpired(task: Task): Boolean {
        return isDateExpired(task.deadlineDate) && !task.isFinished
    }

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            val task = taskRepository.getTaskBy(taskId)
            oldTask = task
            _taskUiState.update {
                it.copy(
                    id = taskId,
                    description = task.description,
                    deadlineDate = task.deadlineDate ?: "",
                    category = task.categoryName,
                    isFinished = task.isFinished
                )
            }
        }
    }

    fun onEvent(event: ToDoListEvent) {
        when (event) {
            is ToDoListEvent.DeleteTask -> {
                viewModelScope.launch {
                    taskRepository.deleteTask(event.task)
                    taskRepository.decrementTaskCount(event.task.categoryName)
                    if (event.task.isFinished) {
                        taskRepository.decrementFinishedTasksCount(event.task.categoryName)
                    }
                }
            }

            is ToDoListEvent.FindTasksBy -> {
                viewModelScope.launch {
                    if (event.category.isEmpty() || event.category == "All") {
                        getAllTasks()
                        return@launch
                    }

                    taskRepository.findTasks(event.category).collectLatest { result ->
                        _taskUiState.update { taskState ->
                            taskState.copy(allTasks = result)
                        }
                    }
                }
            }

            is ToDoListEvent.UpsertTask -> {
                if (_taskUiState.value.description.isEmpty()) {
                    _taskUiState.update {
                        it.copy(descriptionEmptyError = true)
                    }

                    viewModelScope.launch {
                        _taskSaveResult.emit(false)
                    }

                    return
                }

                viewModelScope.launch {
                    val task = if (_taskUiState.value.id != 0) {
                        if (oldTask != null && oldTask?.categoryName != _taskUiState.value.category) {
                            // Update taskCounts for category change
                            taskRepository.decrementTaskCount(oldTask?.categoryName!!)
                            taskRepository.incrementTaskCount(_taskUiState.value.category)
                        }

                        if (oldTask != null && oldTask?.isFinished == false && _taskUiState.value.isFinished) {
                            taskRepository.incrementFinishedTasksCount(_taskUiState.value.category)
                        }

                        if (oldTask != null && oldTask?.isFinished == true && !_taskUiState.value.isFinished) {
                            taskRepository.decrementFinishedTasksCount(_taskUiState.value.category)
                        }

                        if (oldTask != null && isTaskExpired(task = oldTask!!) && !isDateExpired(_taskUiState.value.deadlineDate)) {
                            taskRepository.decrementExpiredTasksCount(_taskUiState.value.category)
                        }

                        if (oldTask != null && !isTaskExpired(task = oldTask!!) && isDateExpired(_taskUiState.value.deadlineDate)) {
                            taskRepository.incrementExpiredTasksCount(_taskUiState.value.category)
                        }

                        Task(
                            id = taskUiState.value.id,
                            description = _taskUiState.value.description,
                            deadlineDate = _taskUiState.value.deadlineDate,
                            isFinished = _taskUiState.value.isFinished,
                            categoryName = _taskUiState.value.category
                        )

                    } else {
                        if (_taskUiState.value.isFinished) {
                            taskRepository.incrementFinishedTasksCount(_taskUiState.value.category)
                        }

                        if (isDateExpired(_taskUiState.value.deadlineDate)) {
                            taskRepository.incrementExpiredTasksCount(_taskUiState.value.category)
                        }

                        taskRepository.incrementTaskCount(_taskUiState.value.category)

                        Task(
                            description = _taskUiState.value.description,
                            deadlineDate = _taskUiState.value.deadlineDate,
                            isFinished = _taskUiState.value.isFinished,
                            categoryName = _taskUiState.value.category
                        )
                    }

                    taskRepository.upsertTask(task)
                    _taskSaveResult.emit(true)                }
            }

            is ToDoListEvent.SetTaskCategory -> {
                if (event.category == "All" || event.category.isEmpty()) {
                    _taskUiState.update {
                        it.copy(category = "Default")
                    }
                    return
                }

                _taskUiState.update {
                    it.copy(category = event.category)
                }
            }

            is ToDoListEvent.SetDeadline -> {
                _taskUiState.update {
                    it.copy(deadlineDate = event.deadline)
                }
            }

            is ToDoListEvent.SetDescription -> {
                _taskUiState.update {
                    it.copy(description = event.description, descriptionEmptyError = false)
                }
            }

            is ToDoListEvent.AddNewCategory -> {
                _categoryUiState.value.categories.forEach {
                    if (it.name == event.newCategory) {
                        return
                    }
                }

                viewModelScope.launch {
                    taskRepository.insertCategory(Category(name = event.newCategory))
                }

                _categoryUiState.update {
                    it.copy(selectedFilterCategory = event.newCategory)
                }
            }

            is ToDoListEvent.FinishTask -> {
                viewModelScope.launch {
                    taskRepository.upsertTask(event.task)
                    taskRepository.incrementFinishedTasksCount(event.task.categoryName)
                    taskRepository.decrementExpiredTasksCount(event.task.categoryName)
                }

                _taskUiState.update {
                    it.copy(isFinished = true)
                }
            }

            is ToDoListEvent.SetFilterCategory -> {
                _categoryUiState.update {
                    it.copy(selectedFilterCategory = event.filterCategory)
                }
            }

            is ToDoListEvent.SetFinish -> {
                _taskUiState.update {
                    it.copy(isFinished = event.isFinished)
                }
            }

            is ToDoListEvent.DeleteCategory -> {
                viewModelScope.launch {
                    taskRepository.deleteCategory(category = event.category)
                }
            }

            is ToDoListEvent.DeleteCategoryFinishedTasks -> {
                viewModelScope.launch {
                    taskRepository.findTasks(event.category.name).collect { tasks ->
                        tasks.forEach {
                            if (it.isFinished) {
                                taskRepository.deleteTaskAndDecrementFinishedTasksCount(
                                    it,
                                    it.categoryName
                                )
                            }
                        }
                    }
                }
            }

            is ToDoListEvent.FinishCategoryTasks -> {
                viewModelScope.launch {
                    taskRepository.finishCategoryTasks(event.category.name)
                    val count = taskRepository.getFinishedTaskCount(event.category.name)
                    taskRepository.updateFinishedTaskCount(event.category.name, count)
                }
            }

            is ToDoListEvent.DeleteCategoryExpiredTasks -> {
                viewModelScope.launch {
                    taskRepository.findTasks(event.category.name).collect { tasks ->
                        tasks.forEach {
                            if (isTaskExpired(it)) {
                                taskRepository.deleteTaskAndDecrementExpiredTasksCount(
                                    it,
                                    it.categoryName
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
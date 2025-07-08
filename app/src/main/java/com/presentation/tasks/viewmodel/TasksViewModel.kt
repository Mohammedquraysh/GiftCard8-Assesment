package com.presentation.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.TaskRepository
import com.domain.model.Task
import com.presentation.common.resource.Resource
import com.presentation.tasks.TasksState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasksState = MutableStateFlow(TasksState())
    val tasksState: StateFlow<TasksState> = _tasksState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        getTasks()
    }

    // get task list
    private fun getTasks() {
        viewModelScope.launch {
            taskRepository.getTasks()
                .take(2)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _tasksState.value = _tasksState.value.copy(
                                tasks = result.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            _tasksState.value = _tasksState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            _tasksState.value = _tasksState.value.copy(isLoading = true)
                        }
                    }
                }
        }
    }

    // add task
    fun addTask(title: String) {
        viewModelScope.launch {
            taskRepository.addTask(title).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _tasksState.value = _tasksState.value.copy(
                            showAddDialog = false,
                            isAddingTask = false
                        )
                        val currentTasks = _tasksState.value.tasks.toMutableList()
                        result.data?.let { newTask ->
                            currentTasks.add(0, newTask)
                            _tasksState.value = _tasksState.value.copy(tasks = currentTasks)
                        }
                        _toastMessage.value = "Task added successfully!"
                    }
                    is Resource.Error -> {
                        _tasksState.value = _tasksState.value.copy(
                            error = result.message,
                            isAddingTask = false
                        )
                    }
                    is Resource.Loading -> {
                        _tasksState.value = _tasksState.value.copy(isAddingTask = true)
                    }
                }
            }
        }
    }

    // update task
    fun toggleTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val updatedTasks = _tasksState.value.tasks.map { existingTask ->
                            if (existingTask.id == task.id) {
                                existingTask.copy(isCompleted = !existingTask.isCompleted)
                            } else {
                                existingTask
                            }
                        }
                        _tasksState.value = _tasksState.value.copy(
                            tasks = updatedTasks,
                            isUpdatingTask = false,
                            updatingTaskId = null
                        )
                        val message = if (!task.isCompleted) "Task completed!" else "Task marked as incomplete"
                        _toastMessage.value = message
                    }
                    is Resource.Error -> {
                        _tasksState.value = _tasksState.value.copy(
                            error = result.message,
                            isUpdatingTask = false,
                            updatingTaskId = null
                        )
                    }
                    is Resource.Loading -> {
                        _tasksState.value = _tasksState.value.copy(
                            isUpdatingTask = true,
                            updatingTaskId = task.id
                        )
                    }
                }
            }
        }
    }

    // delete task
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val updatedTasks = _tasksState.value.tasks.filter { it.id != taskId }
                        _tasksState.value = _tasksState.value.copy(
                            tasks = updatedTasks,
                            isDeletingTask = false,
                            deletingTaskId = null
                        )
                        _toastMessage.value = "Task deleted successfully!"
                    }
                    is Resource.Error -> {
                        _tasksState.value = _tasksState.value.copy(
                            error = result.message,
                            isDeletingTask = false,
                            deletingTaskId = null
                        )
                    }
                    is Resource.Loading -> {
                        _tasksState.value = _tasksState.value.copy(
                            isDeletingTask = true,
                            deletingTaskId = taskId
                        )
                    }
                }
            }
        }
    }

    fun showAddDialog() {
        _tasksState.value = _tasksState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _tasksState.value = _tasksState.value.copy(showAddDialog = false)
    }

    fun clearError() {
        _tasksState.value = _tasksState.value.copy(error = null)
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _tasksState.value = TasksState()
                onSuccess()
            } catch (e: Exception) {
                _tasksState.value = _tasksState.value.copy(error = "Logout failed")
            }
        }
    }
}
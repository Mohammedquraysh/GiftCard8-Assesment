package com.presentation.tasks

import com.domain.model.Task

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val isAddingTask: Boolean = false,
    val isUpdatingTask: Boolean = false,
    val isDeletingTask: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false,
    val updatingTaskId: String? = null,
    val deletingTaskId: String? = null
)

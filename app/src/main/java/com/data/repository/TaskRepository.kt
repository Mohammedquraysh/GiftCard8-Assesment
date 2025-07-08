package com.data.repository

import com.data.local.dao.TaskDao
import com.data.local.entities.TaskEntity
import com.data.remote.api.TaskApi
import com.data.remote.dto.TaskDto.CreateTaskRequest
import com.data.remote.dto.TaskDto.UpdateTaskRequest
import com.domain.model.Task
import com.presentation.common.resource.Resource
import com.util.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskApi: TaskApi,
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) {

    fun getTasks(): Flow<Resource<List<Task>>> = flow {
        val userId = preferencesManager.getUserId() ?: throw Exception("User not logged in")

        emit(Resource.Loading())

        // Emit cached data first (limit to 1 emission)
        taskDao.getTasksByUserId(userId).take(1).collect { entities ->
            val tasks = entities.map { entity ->
                Task(
                    id = entity.id,
                    title = entity.title,
                    isCompleted = entity.isCompleted,
                    createdAt = entity.createdAt,
                    userId = entity.userId
                )
            }
            emit(Resource.Success(tasks))
        }

        // Fetch from API moved outside collect
        val response = taskApi.getTodos(30)
        if (response.isSuccessful) {
            response.body()?.let { tasksResponse ->
                val taskEntities = tasksResponse.todos.map { dto ->
                    TaskEntity(
                        id = dto.id.toString(),
                        title = dto.todo,
                        isCompleted = dto.completed,
                        createdAt = Date(),
                        userId = userId
                    )
                }

                taskDao.deleteTasksByUserId(userId)
                taskDao.insertTasks(taskEntities)

                // Emit final updated tasks
                val updatedTasks = taskEntities.map { entity ->
                    Task(
                        id = entity.id,
                        title = entity.title,
                        isCompleted = entity.isCompleted,
                        createdAt = entity.createdAt,
                        userId = entity.userId
                    )
                }
                emit(Resource.Success(updatedTasks))
            }
        }
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Failed to fetch tasks"))
    }

     fun addTask(title: String): Flow<Resource<Task>> = flow {
        val userId = preferencesManager.getUserId() ?: throw Exception("User not logged in")
        val taskId = UUID.randomUUID().toString()

        emit(Resource.Loading())

        val task = Task(
            id = taskId,
            title = title,
            isCompleted = false,
            createdAt = Date(),
            userId = userId
        )

        // Save locally first
        taskDao.insertTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                isCompleted = task.isCompleted,
                createdAt = task.createdAt,
                userId = task.userId
            )
        )

        // Try to sync to API
        try {
            taskApi.createTask(
                CreateTaskRequest(
                    todo = title,
                    completed = false,
                    userId = userId.toInt()
                )
            )
        } catch (e: Exception) {
        }

        emit(Resource.Success(task))
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Failed to add task"))
    }

     fun updateTask(task: Task): Flow<Resource<Task>> = flow {
        emit(Resource.Loading())

        // Update locally first
        taskDao.updateTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                isCompleted = task.isCompleted,
                createdAt = task.createdAt,
                userId = task.userId
            )
        )

        // Try to sync to API
        try {
            taskApi.updateTask(
                task.id,
                UpdateTaskRequest(completed = task.isCompleted)
            )
        } catch (e: Exception) {
        }

        emit(Resource.Success(task))
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Failed to update task"))
    }


     fun deleteTask(taskId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        // Delete locally first
        taskDao.deleteTaskById(taskId)

        // Try to sync to API
        try {
            taskApi.deleteTask(taskId)
        } catch (e: Exception) {
        }

        emit(Resource.Success(Unit))
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Failed to delete task"))
    }
}
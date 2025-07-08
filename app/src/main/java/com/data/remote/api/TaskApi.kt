package com.data.remote.api

import com.data.remote.dto.TaskDto.CreateTaskRequest
import com.data.remote.dto.TaskDto.DeleteTaskResponse
import com.data.remote.dto.TaskDto.TaskDto
import com.data.remote.dto.TaskDto.TasksResponse
import com.data.remote.dto.TaskDto.UpdateTaskRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    @GET("todos")
    suspend fun getTodos(@Query("limit") limit: Int = 30): Response<TasksResponse>

    @POST("todos/add")
    suspend fun createTask(@Body request: CreateTaskRequest): Response<TaskDto>

    @PUT("todos/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Body request: UpdateTaskRequest
    ): Response<TaskDto>

    @DELETE("todos/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<DeleteTaskResponse>
}
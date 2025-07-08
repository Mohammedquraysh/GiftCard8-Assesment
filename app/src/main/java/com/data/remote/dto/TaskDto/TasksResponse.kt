package com.data.remote.dto.TaskDto

import com.google.gson.annotations.SerializedName

data class TasksResponse(
    @SerializedName("todos")
    val todos: List<TaskDto>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int
)

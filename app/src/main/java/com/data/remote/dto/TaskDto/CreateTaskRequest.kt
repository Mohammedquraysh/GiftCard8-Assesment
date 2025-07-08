package com.data.remote.dto.TaskDto

import com.google.gson.annotations.SerializedName

data class CreateTaskRequest(
    @SerializedName("todo")
    val todo: String,
    @SerializedName("completed")
    val completed: Boolean = false,
    @SerializedName("userId")
    val userId: Int
)
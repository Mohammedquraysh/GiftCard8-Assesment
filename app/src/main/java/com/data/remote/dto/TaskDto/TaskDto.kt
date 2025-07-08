package com.data.remote.dto.TaskDto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("todo")
    val todo: String,
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("userId")
    val userId: Int
)

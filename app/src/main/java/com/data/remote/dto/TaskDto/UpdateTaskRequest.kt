package com.data.remote.dto.TaskDto

import com.google.gson.annotations.SerializedName

data class UpdateTaskRequest(
    @SerializedName("completed")
    val completed: Boolean
)

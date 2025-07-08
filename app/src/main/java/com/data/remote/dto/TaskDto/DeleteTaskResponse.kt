package com.data.remote.dto.TaskDto

import com.google.gson.annotations.SerializedName

data class DeleteTaskResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("todo")
    val todo: String,
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("deletedOn")
    val deletedOn: String
)
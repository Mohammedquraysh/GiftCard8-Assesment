package com.domain.model

import java.util.Date

data class Task(
    val id: String = "",
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val userId: String = ""
)

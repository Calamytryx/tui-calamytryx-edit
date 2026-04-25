package com.rama.tui

data class Task(
    val id: Long = 0,
    val stepId: Long = 0,
    var label: String,
    var duration: Int = 0
)

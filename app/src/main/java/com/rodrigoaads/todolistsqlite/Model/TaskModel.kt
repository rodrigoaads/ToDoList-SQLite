package com.rodrigoaads.todolistsqlite.Model

class TaskModel(
    val taskId: Int = 0,
    var taskName: String = "",
    var taskStatus: TaskStatus
) {
}
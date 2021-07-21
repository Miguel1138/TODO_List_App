package com.miguelsantos.todewit.datasource

import com.miguelsantos.todewit.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list

    fun insertTask(task: Task) {
        if (task.id == 0) {
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findById(taskId: Int) = list.find { task -> taskId == task.id }

    fun deleteTask(task: Task) { list.remove(task) }
}
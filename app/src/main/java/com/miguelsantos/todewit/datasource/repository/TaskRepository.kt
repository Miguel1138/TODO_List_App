package com.miguelsantos.todewit.datasource.repository

import com.miguelsantos.todewit.datasource.model.Task
import com.miguelsantos.todewit.datasource.room.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    val list: List<Task> = taskDao.getAll()

    fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    fun deleteCompletedTasks() {
        taskDao.deleteAllCompleted()
    }

    fun findById(taskId: Int) {
        taskDao.findById(taskId)
    }

}
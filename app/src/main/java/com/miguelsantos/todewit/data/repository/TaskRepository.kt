package com.miguelsantos.todewit.data.repository

import com.miguelsantos.todewit.data.localdatasource.TaskDao
import com.miguelsantos.todewit.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val list: Flow<List<Task>> = taskDao.getAll()

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

}
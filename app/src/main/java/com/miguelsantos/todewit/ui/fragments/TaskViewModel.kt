package com.miguelsantos.todewit.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.miguelsantos.todewit.datasource.model.Task
import com.miguelsantos.todewit.datasource.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    val task: Task?
) : ViewModel() {

    val taskList: LiveData<List<Task>> = taskRepository.list.asLiveData()

    internal fun insertTask(task: Task) = viewModelScope.launch { insert(task) }

    private suspend fun insert(newTask: Task) {
        taskRepository.insertTask(newTask)
    }

    internal fun updateTask(task: Task) = viewModelScope.launch { update(task) }

    private suspend fun update(task: Task) {
        taskRepository.updateTask(task)
    }

    internal fun deleteTask(task: Task) = viewModelScope.launch { delete(task) }

    private suspend fun delete(task: Task) {
        taskRepository.deleteTask(task)
    }

    internal fun findById() = viewModelScope.launch { find(task?.id) }

    private suspend fun find(taskId: Int?) {
        taskRepository.findById(taskId ?: throw Exception("Task not found"))
    }

    internal fun deleteCompletedTasks() = viewModelScope.launch {
        taskRepository.deleteCompletedTasks()
    }

}

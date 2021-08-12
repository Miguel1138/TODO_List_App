package com.miguelsantos.todewit.ui.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.miguelsantos.todewit.data.repository.TaskRepository
import com.miguelsantos.todewit.model.Task
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

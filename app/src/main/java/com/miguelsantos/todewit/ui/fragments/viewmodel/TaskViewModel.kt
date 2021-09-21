package com.miguelsantos.todewit.ui.fragments.viewmodel

import androidx.lifecycle.*
import com.miguelsantos.todewit.data.repository.TaskRepository
import com.miguelsantos.todewit.model.Task
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    val task: Task?
) : ViewModel() {

    val taskList: LiveData<List<Task>> = taskRepository.list.asLiveData()

    internal fun insertTask(task: Task) = viewModelScope.launch { taskRepository.insertTask(task) }

    internal fun updateTask(task: Task) = viewModelScope.launch { taskRepository.updateTask(task) }

    internal fun deleteTask(task: Task) = viewModelScope.launch { taskRepository.deleteTask(task) }

    fun isLayoutEmpty(): Boolean = task == null
}


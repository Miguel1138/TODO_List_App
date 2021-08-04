package com.miguelsantos.todewit.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.miguelsantos.todewit.datasource.model.Task
import com.miguelsantos.todewit.datasource.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository, var task: Task?) : ViewModel() {

    val taskList: LiveData<List<Task>> = taskRepository.list.asLiveData()

    internal fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insertTask(task)
    }

    internal fun findById() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.findById(task?.id ?: -1)
    }

    internal fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteTask(task)
    }

    internal fun deleteCompletedTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteCompletedTasks()
    }

    internal fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTAsk(task)
    }

    // TODO: 03/08/2021 select all não está funcionando, taskList.value está retornando null.
    // Pesquisar sobre pegar dados mutaveis do banco de dados
    internal fun selectAll() {
        val list = taskList.value
        // Uncheck all the checkboxes in case they are all already selected.
        if (list != null) {
            if (list.all { it.isDone == 1 }) {
                list.forEach { it.isDone = 0 }
            } else {
                list.filter { it.isDone == 0 }
                    .forEach { it.isDone = 1 }
            }
        }
    }

}

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

    internal fun insertTask(task: Task) = viewModelScope.launch { taskRepository.insertTask(task) }

    internal fun updateTask(task: Task) = viewModelScope.launch { taskRepository.updateTask(task) }

    internal fun deleteTask(task: Task) = viewModelScope.launch { taskRepository.deleteTask(task) }

    internal fun clearCompletedTasks() = viewModelScope.launch {
        taskRepository.clearCompletedTasks()
    }

    fun completeTask(taskId: Int, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(taskId, completed)
    }

    internal fun checkAllItems(completed: Boolean, ruleSet: Int = 0) = viewModelScope.launch {
        taskRepository.checkAllItems(completed, ruleSet)
    }

    fun isLayoutEmpty(): Boolean = task == null

    // TODO Rever essa parte, CheckBox não está atualizando, talvez trocar o campo checked por outro.
    fun areAllItemsChecked(): Boolean = taskList.value!!.all { it.isDone }

}


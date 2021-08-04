package com.miguelsantos.todewit.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miguelsantos.todewit.datasource.model.Task
import com.miguelsantos.todewit.datasource.repository.TaskRepository

class TaskViewModelFactory(
    private val repository: TaskRepository,
    private val task: Task? = null
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(repository, task) as T

        throw IllegalArgumentException("Unknown view model class")
    }

}
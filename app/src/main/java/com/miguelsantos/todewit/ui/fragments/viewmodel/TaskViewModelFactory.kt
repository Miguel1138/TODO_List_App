package com.miguelsantos.todewit.ui.fragments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miguelsantos.todewit.data.repository.TaskRepository
import com.miguelsantos.todewit.model.Task

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
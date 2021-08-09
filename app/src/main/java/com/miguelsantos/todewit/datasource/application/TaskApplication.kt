package com.miguelsantos.todewit.datasource.application

import android.app.Application
import com.miguelsantos.todewit.datasource.database.AppDatabase
import com.miguelsantos.todewit.datasource.repository.TaskRepository

class TaskApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }

}
package com.miguelsantos.todewit.data.application

import android.app.Application
import com.miguelsantos.todewit.data.localdatasource.AppDatabase
import com.miguelsantos.todewit.data.repository.TaskRepository

class TaskApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }

}
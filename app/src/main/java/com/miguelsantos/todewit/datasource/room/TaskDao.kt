package com.miguelsantos.todewit.datasource.room

import androidx.room.*
import com.miguelsantos.todewit.datasource.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Insert
    fun insertTask(task:Task)

    @Update
    fun updateTask(task:Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("DELETE FROM task WHERE isDone = 1")
    fun deleteAllCompleted()

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun findById(taskId: Int)
}
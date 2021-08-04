package com.miguelsantos.todewit.datasource.database

import androidx.room.*
import com.miguelsantos.todewit.datasource.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("DELETE FROM task WHERE isDone = 1")
    fun deleteAllCompleted()

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun findById(taskId: Int): Task

    @Update
    fun updateTask(task: Task)

}
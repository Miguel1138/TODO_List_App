package com.miguelsantos.todewit.data.localdatasource

import androidx.room.*
import com.miguelsantos.todewit.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY id DESC")
    fun getAll(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task WHERE isDone = 1")
    suspend fun deleteAllCompleted()

    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun findById(taskId: Int): Task

    @Update
    suspend fun updateTask(task: Task)

}
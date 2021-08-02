package com.miguelsantos.todewit.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miguelsantos.todewit.datasource.model.Task
import com.miguelsantos.todewit.util.constants.Constants.taskDbConfig.DATABASE_NAME
import com.miguelsantos.todewit.util.constants.Constants.taskDbConfig.DATABASE_VERSION

@Database(entities = [Task::class], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE
            ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext)
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }
}
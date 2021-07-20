package com.miguelsantos.todewit.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.miguelsantos.todewit.databinding.ActivityMainBinding
import com.miguelsantos.todewit.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.mainFabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, CREATE_MEW_TASK)
        }
        adapter.listenerEdit = {
            Log.i("TAG", "listenerEdit: $it")
        }
        adapter.listenerDelete = {
            Log.i("TAG", "listenerDelete: $it")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_MEW_TASK) {
            binding.mainRecyclerTasks.adapter = adapter
            adapter.submitList(TaskDataSource.getList())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val CREATE_MEW_TASK = 1010
    }

}
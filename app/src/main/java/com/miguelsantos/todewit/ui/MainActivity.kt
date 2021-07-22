package com.miguelsantos.todewit.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.miguelsantos.todewit.databinding.ActivityMainBinding
import com.miguelsantos.todewit.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_MEW_TASK = 1010
    }

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainRecyclerTasks.adapter = adapter
        updateList()

        setListeners()
    }

    private fun setListeners() {
        binding.mainFabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, CREATE_MEW_TASK)
        }

        adapter.listenerEdit = { task ->
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, task.id)
            startActivityForResult(intent, CREATE_MEW_TASK)
        }

        adapter.listenerDelete = { task ->
            TaskDataSource.deleteTask(task)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_MEW_TASK && resultCode == Activity.RESULT_OK) updateList()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.layoutEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        adapter.notifyDataSetChanged()
        adapter.submitList(list)
    }
    
}
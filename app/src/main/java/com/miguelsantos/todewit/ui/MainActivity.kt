package com.miguelsantos.todewit.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.miguelsantos.todewit.R
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

        supportActionBar.apply {
            setSupportActionBar(binding.mainToolbar)
            binding.mainToolbar.overflowIcon =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_more_horiz_24)
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_all -> {
                val list = TaskDataSource.getList()
                // Uncheck all the checkboxes in case they are all already selected.
                if (list.all { it.isDone }) {
                    list.forEach { it.isDone = false }
                } else {
                    list.filter { (!it.isDone) }
                        .forEach { it.isDone = true }
                }
                updateList()
                true
            }
            R.id.action_clear_finished_tasks -> {
                TaskDataSource.getList().apply { removeAll(filter { it.isDone }) }
                updateList()
                true
            }
            else -> false
        }
    }

}
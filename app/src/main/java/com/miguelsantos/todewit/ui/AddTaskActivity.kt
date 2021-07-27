package com.miguelsantos.todewit.ui

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.ActivityAddTaskBinding
import com.miguelsantos.todewit.datasource.TaskDataSource
import com.miguelsantos.todewit.extensions.format
import com.miguelsantos.todewit.extensions.formatTime
import com.miguelsantos.todewit.extensions.text
import com.miguelsantos.todewit.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    companion object {
        private const val DATE_PICKER_TAG: String = "date_picker_tag"
        private const val TIME_PICKER_TAG: String = "time_picker_tag"
        const val TASK_ID = "task_id"
    }

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar.apply {
            setSupportActionBar(binding.taskToolbar)
            this?.setHomeButtonEnabled(true)
        }

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let { task ->
                binding.taskInputLayoutTitle.text = task.title
                binding.taskInputLayoutDescription.text = task.description
                binding.taskInputLayoutTime.text = task.hour
                binding.taskInputLayoutDate.text = task.date
            }
            binding.taskBtnCreateTask.text = getString(R.string.edit_task)
            binding.taskToolbar.title = getString(R.string.edit_task)
        }
        setListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setListeners() {
        // Date
        binding.taskInputLayoutDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.taskInputLayoutDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)
        }

        // Time Picker
        binding.taskInputLayoutTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = formatTime(timePicker.hour)
                val minute = formatTime(timePicker.minute)

                binding.taskInputLayoutTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, TIME_PICKER_TAG)
        }

        // Cancel Button
        binding.taskBtnCancel.setOnClickListener {
            finish()
        }

        // Create Task button
        binding.taskBtnCreateTask.setOnClickListener {
            val task = Task(
                title = binding.taskInputLayoutTitle.text,
                description = binding.taskInputLayoutDescription.text,
                date = binding.taskInputLayoutDate.text,
                hour = binding.taskInputLayoutTime.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}
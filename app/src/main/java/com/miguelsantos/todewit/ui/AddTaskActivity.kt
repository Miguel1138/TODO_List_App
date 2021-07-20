package com.miguelsantos.todewit.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.miguelsantos.todewit.databinding.ActivityAddTaskBinding
import com.miguelsantos.todewit.datasource.TaskDataSource
import com.miguelsantos.todewit.extensions.format
import com.miguelsantos.todewit.extensions.text
import com.miguelsantos.todewit.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    companion object {
        private const val DATE_PICKER_TAG: String = "date_picker_tag"
        private const val TIME_PICKER_TAG: String = "time_picker_tag"
    }

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
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
                val hour =
                    if (timePicker.hour in 0..9) "0${timePicker.hour}" else "${timePicker.hour}"

                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else "${timePicker.minute}"

                binding.taskInputLayoutTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, TIME_PICKER_TAG)
        }

        // Cancel
        binding.taskBtnCancel.setOnClickListener {
            finish()
        }

        // Create Task
        binding.taskBtnCreateTask.setOnClickListener {
            val task = Task(
                title = binding.taskInputLayoutTitle.text,
                date = binding.taskInputLayoutDate.text,
                hour = binding.taskInputLayoutTime.text
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}
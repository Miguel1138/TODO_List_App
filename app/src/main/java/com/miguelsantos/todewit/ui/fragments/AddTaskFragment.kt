package com.miguelsantos.todewit.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.FragmentAddTaskBinding
import com.miguelsantos.todewit.datasource.TaskDataSource
import com.miguelsantos.todewit.extensions.format
import com.miguelsantos.todewit.extensions.formatTime
import com.miguelsantos.todewit.extensions.text
import com.miguelsantos.todewit.model.Task
import java.util.*


class AddTaskFragment : Fragment() {

    companion object {
        private const val DATE_PICKER_TAG: String = "date_picker_tag"
        private const val TIME_PICKER_TAG: String = "time_picker_tag"
        const val TASK_ID = "task_id"
    }

    private val bundle: String? = null
    private val activity by lazy {
        requireActivity() as AppCompatActivity
    }
    private lateinit var binding: FragmentAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        /*if (activity.intent.hasExtra(TASK_ID)) {
            val taskId = activity.intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let { task ->
                binding.taskInputLayoutTitle.text = task.title
                binding.taskInputLayoutDescription.text = task.description
                binding.taskInputLayoutTime.text = task.hour
                binding.taskInputLayoutDate.text = task.date
            }
            binding.taskBtnCreateTask.text = getString(R.string.edit_task)
            //binding..title = getString(R.string.edit_task)
        }*/
        //val args = AddTaskFragmentArgs.fromBundle(requireArguments())

        val bundle = this.arguments
       if (bundle != null) {
           val taskId = bundle.getInt(TASK_ID)
           TaskDataSource.findById(taskId)?.let { task ->
               binding.taskInputLayoutTitle.text = task.title
               binding.taskInputLayoutDescription.text = task.description
               binding.taskInputLayoutTime.text = task.hour
               binding.taskInputLayoutDate.text = task.date
           }
           binding.taskBtnCreateTask.text = getString(R.string.edit_task)
       }
        setListeners()

        return binding.root
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
            datePicker.show(parentFragmentManager, DATE_PICKER_TAG)
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
            timePicker.show(parentFragmentManager, TIME_PICKER_TAG)
        }

        // Cancel Button
        binding.taskBtnCancel.setOnClickListener {
            it.findNavController().navigate(
                AddTaskFragmentDirections.actionAddTaskFragmentToHomeFragment()
            )
        }

        // Create Task button
        binding.taskBtnCreateTask.setOnClickListener { addTask(it) }
    }

    private fun addTask(view: View) {
        val task = Task(
            title = binding.taskInputLayoutTitle.text,
            description = binding.taskInputLayoutDescription.text,
            date = binding.taskInputLayoutDate.text,
            hour = binding.taskInputLayoutTime.text
            //id = activity.intent.getIntExtra(TASK_ID, 0)
        )
        TaskDataSource.insertTask(task)
        activity.setResult(Activity.RESULT_OK)
        view.findNavController().navigate(
            AddTaskFragmentDirections.actionAddTaskFragmentToHomeFragment()
        )
    }
}
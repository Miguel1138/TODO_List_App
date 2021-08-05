package com.miguelsantos.todewit.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.FragmentAddTaskBinding
import com.miguelsantos.todewit.datasource.application.TaskApplication
import com.miguelsantos.todewit.datasource.model.Task
import com.miguelsantos.todewit.ui.fragments.TaskViewModel
import com.miguelsantos.todewit.ui.fragments.TaskViewModelFactory
import com.miguelsantos.todewit.util.extensions.format
import com.miguelsantos.todewit.util.extensions.formatTime
import com.miguelsantos.todewit.util.extensions.text
import java.util.*


class AddTaskFragment : Fragment() {

    companion object {
        private const val DATE_PICKER_TAG: String = "date_picker_tag"
        private const val TIME_PICKER_TAG: String = "time_picker_tag"
    }

    private val activity by lazy {
        requireActivity() as AppCompatActivity
    }
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var viewModelFactory: TaskViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        val repository = (activity.application as TaskApplication).repository
        val editTask = AddTaskFragmentArgs.fromBundle(requireArguments()).task

        viewModelFactory = TaskViewModelFactory(repository, editTask)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        if (editTask != null) {
            viewModel.findById().let { task ->
                binding.taskInputLayoutTitle.text = viewModel.task?.title ?: ""
                binding.taskInputLayoutDescription.text = viewModel.task?.description ?: ""
                binding.taskInputLayoutTime.text = viewModel.task?.hour ?: ""
                binding.taskInputLayoutDate.text = viewModel.task?.date ?: ""
            }
            // Edit Task Layout
            binding.taskBtnCreateTask.text = getString(R.string.edit_task)
            binding.taskBtnCreateTask.setOnClickListener { updateTask(it) }
            activity.supportActionBar?.title = getString(R.string.edit_task)
        } else {
            // Create Task button
            activity.supportActionBar?.title = getString(R.string.label_create_task)
            binding.taskBtnCreateTask.setOnClickListener { addTask(it) }
        }
        setListeners()

        return binding.root
    }

    /**
     * Click Listeners
     */
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
    }

    private fun addTask(view: View) {
        val task = createTaskObject()
        viewModel.insertTask(task)
        view.findNavController().navigate(
            AddTaskFragmentDirections.actionAddTaskFragmentToHomeFragment()
        )
    }

    private fun updateTask(view: View) {
        val task = createTaskObject()
        viewModel.updateTask(task)
        view.findNavController().navigate(
            AddTaskFragmentDirections.actionAddTaskFragmentToHomeFragment()
        )
    }

    /**
     * O Id vai mudar caso seja um objeto já criado ou um novo objeto
     */
    private fun createTaskObject(): Task = Task(
        title = binding.taskInputLayoutTitle.text,
        description = binding.taskInputLayoutDescription.text,
        date = binding.taskInputLayoutDate.text,
        hour = binding.taskInputLayoutTime.text,
        id = viewModel.task?.id ?: 0
    )

}
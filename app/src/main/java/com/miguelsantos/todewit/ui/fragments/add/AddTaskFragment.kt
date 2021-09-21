package com.miguelsantos.todewit.ui.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.data.application.TaskApplication
import com.miguelsantos.todewit.databinding.FragmentAddTaskBinding
import com.miguelsantos.todewit.model.Task
import com.miguelsantos.todewit.ui.fragments.viewmodel.TaskViewModel
import com.miguelsantos.todewit.ui.fragments.viewmodel.TaskViewModelFactory
import com.miguelsantos.todewit.util.extensions.format
import com.miguelsantos.todewit.util.extensions.formatTime
import com.miguelsantos.todewit.util.extensions.text
import java.util.*


class AddTaskFragment : Fragment() {

    companion object {
        private const val DATE_PICKER_TAG: String = "date_picker_tag"
        private const val TIME_PICKER_TAG: String = "time_picker_tag"
    }

    private var editTask: Task? = null
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var viewModelFactory: TaskViewModelFactory
    private val activity by lazy { requireActivity() as AppCompatActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (activity.application as TaskApplication).repository
        editTask = AddTaskFragmentArgs.fromBundle(requireArguments()).task
        viewModelFactory = TaskViewModelFactory(repository, editTask)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            taskViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            taskFragment = this@AddTaskFragment
        }

        activity.supportActionBar?.title =
            if (viewModel.isLayoutEmpty()) getString(R.string.label_create_task) else getString(R.string.label_edit_task)
    }

    fun timePickerListener() {
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

    // valores estão vindo nulos ao clicar no widget.
    fun datePickerListener() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener {
            val timeZone = TimeZone.getDefault()
            val offset = timeZone.getOffset(Date().time) * -1
            binding.taskInputLayoutDate.text = Date(it + offset).format()
        }
        datePicker.show(parentFragmentManager, DATE_PICKER_TAG)
    }

    fun addTask() {
        val task = createTaskObject()
        viewModel.insertTask(task)
        returnHomeScreen()
    }

    fun updateTask() {
        val task = createTaskObject()
        viewModel.updateTask(task)
        returnHomeScreen()
    }

    fun returnHomeScreen() {
        findNavController().navigate(
            AddTaskFragmentDirections.actionAddTaskFragmentToHomeFragment()
        )
    }

    /**
     * O Id vai mudar caso seja um objeto já criado ou um novo objeto
     */
    private fun createTaskObject() = Task(
        title = binding.taskInputLayoutTitle.text,
        description = binding.taskInputLayoutDescription.text,
        date = binding.taskInputLayoutDate.text,
        hour = binding.taskInputLayoutTime.text,
        id = viewModel.task?.id ?: 0
    )

}
package com.miguelsantos.todewit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.miguelsantos.todewit.databinding.ActivityAddTaskBinding
import com.miguelsantos.todewit.extensions.format
import com.miguelsantos.todewit.extensions.text
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    companion object {
        private const val DATE_PICKER_TAG: String = "date_picker_tag"
    }

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.taskInputLayoutDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.taskInputLayoutDate.text =  Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, Companion.DATE_PICKER_TAG)
        }
    }

}
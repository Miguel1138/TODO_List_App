package com.miguelsantos.todewit.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "BR")

fun Date.format(): String = SimpleDateFormat("dd/MM/yyyy", locale).format(this)

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }

fun formatTime(input: Int):String = if (input in 0..9) "0$input" else "$input"
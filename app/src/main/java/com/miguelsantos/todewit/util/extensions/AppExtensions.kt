package com.miguelsantos.todewit.util.extensions

import android.graphics.Paint
import android.text.format.DateFormat
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale.getDefault()
private val pattern = DateFormat.getBestDateTimePattern(locale, "ddMMyyyy")

fun Date.format(): String = SimpleDateFormat(pattern, locale).format(this)

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }

fun formatTime(input: Int): String = if (input in 0..9) "0$input" else "$input"

var TextView.strike: Boolean
    set(strikeEnabled) {
        paintFlags =
            if (strikeEnabled) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
    get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG == Paint.STRIKE_THRU_TEXT_FLAG
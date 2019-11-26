package com.fiap18Mob.clean.utils

import android.util.Patterns
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.error = if (validator(this.text.toString())) null else message
}

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Long.toDateTime(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val netDate = Date(this)
    return dateFormat.format(netDate)
}
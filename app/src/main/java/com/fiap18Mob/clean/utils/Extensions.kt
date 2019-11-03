package com.fiap18Mob.clean.utils

import android.util.Patterns
import android.widget.EditText

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.error = if (validator(this.text.toString())) null else message
}

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
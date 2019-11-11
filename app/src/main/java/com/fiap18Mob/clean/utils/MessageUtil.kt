package com.fiap18Mob.clean.utils

class MessageUtil {
    companion object {
        fun errorTranslation(errorMsg: String) : String =
            when (errorMsg) {
                "auth/email-already-in-use" -> "Email already in user"
                else -> "Something went wrong"
            }

    }
}
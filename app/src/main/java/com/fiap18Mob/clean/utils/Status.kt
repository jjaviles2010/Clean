package com.fiap18Mob.clean.utils

import com.fiap18Mob.clean.MyApplication
import com.fiap18Mob.clean.R

object Status {

    enum class ServiceStatus(val status: String) {
        OPENED("OPENED"),
        CANCELED("CANCELED"),
        DONE("DONE")
    }

    fun translateStatus(status: String) : String {
        return when (status) {
            "OPENED" -> MyApplication.instance.applicationContext.getString(R.string.OPENED)
            "CANCELED" -> MyApplication.instance.applicationContext.getString(R.string.CANCELED)
            "DONE" -> MyApplication.instance.applicationContext.getString(R.string.DONE)
            "FEITO" -> "DONE"
            "CANCELADO" -> "CANCELED"
            "ABERTO" -> "OPENED"
            else -> status
        }
    }

    fun translateToEnglish(status: String) : String {
        return when (status) {
            "FEITO" -> "DONE"
            "CANCELADO" -> "CANCELED"
            "ABERTO" -> "OPENED"
            else -> status
        }
    }
}
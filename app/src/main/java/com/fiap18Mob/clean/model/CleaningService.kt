package com.fiap18Mob.clean.model

data class CleaningService (
    var cleanerCPF: String = "",
    var cleaningStatus: String = "",
    var scheduledTime: Long= 0
    )
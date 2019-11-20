package com.fiap18Mob.clean.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CleaningService (
    var cleanerCPF: String = "",
    var cleaningStatus: String = "",
    var scheduledTime: Long= 0
    ) : Parcelable
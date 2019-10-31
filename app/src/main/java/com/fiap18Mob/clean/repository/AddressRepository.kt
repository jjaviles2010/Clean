package com.fiap18Mob.clean.repository

import com.fiap18Mob.clean.model.Address

interface AddressRepository {

    fun getAddress(
        cep: String,
        onComplete: (Address?) -> Unit,
        onError: (Throwable?) -> Unit
        )

}
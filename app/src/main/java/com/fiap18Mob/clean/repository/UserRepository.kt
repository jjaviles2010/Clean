package com.fiap18Mob.clean.repository

import com.fiap18Mob.clean.model.User

interface UserRepository {
    suspend fun insertUser(
        user: User
    )

    fun getUser(
        cpf: String
    )
}
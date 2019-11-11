package com.fiap18Mob.clean.repository

import androidx.lifecycle.LiveData
import com.fiap18Mob.clean.dao.UserDao
import com.fiap18Mob.clean.model.User

interface UserRepository {
    suspend fun insertUser(
        user: User,
        onComplete: (Boolean?) -> Unit,
        onError: (Throwable?) -> Unit
    )

    fun getUser(
        cpf: String
    ): User
}